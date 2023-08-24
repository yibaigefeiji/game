package com.lijian.game.flashsale.service.impl;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


import com.alibaba.nacos.client.utils.JSONUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.lijian.game.common.to.SeckillOrderTo;
import com.lijian.game.common.utils.R;
import com.lijian.game.flashsale.DTO.UserDTO;
import com.lijian.game.flashsale.feign.ProductFeignService;
import com.lijian.game.flashsale.feign.SeckillFeignService;
import com.lijian.game.flashsale.service.SeckillService;
import com.lijian.game.flashsale.to.SeckillSkuRedisTo;
import com.lijian.game.flashsale.utils.UserHolder;
import com.lijian.game.flashsale.vo.SeckillProductVo;
import com.lijian.game.flashsale.vo.SkuInfoVo;
import java.util.Collections;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author lijian
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    private final String SECKILL_CHARE_PREFIX = "seckill:skus";
    //+商品随机码
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Autowired
    private SeckillFeignService seckillFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void uploadSeckillSku3Days() {
        //1、扫描最近三天的商品需要参加秒杀的活动
        R product = seckillFeignService.get3DayProduct();
        if(product.getCode() == 0){

            //上架商品（redis）
            List<SeckillProductVo> seckillProductVos = product.getData("data",new TypeReference<List<SeckillProductVo>>(){});

            //缓存活动的商品信息
            saveProductSkuInfo(seckillProductVos);

        }

    }

    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillProducts() {
        //从redis查询所有的商品进行判断
        BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);
        List<Object> values = cartOps.values();
        if (values != null && values.size() > 0) {
            List<SeckillSkuRedisTo> collect = values.stream().map(obj -> {
                String str = (String) obj;
                return JSON.parseObject(str, SeckillSkuRedisTo.class);
            }).collect(Collectors.toList());

            long currentTime = System.currentTimeMillis();
            return collect
                    .stream()
                    .filter(item -> currentTime >= item.getStartTime() && currentTime <= item.getEndTime())
                    .collect(Collectors.toList());
        }else {
            return Collections.emptyList();
        }

    }

    @Override
    public String kill(String id, String randomCode) throws InterruptedException {
        long s1 = System.currentTimeMillis();

        //获取当前用户的信息
        UserDTO userDTO = UserHolder.getUser();

        //1.从redis中获取当前需要秒杀的商品
        BoundHashOperations<String,Object,Object> operations = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);
        Object skuInfoValue = operations.get(id);
        if (StringUtils.isEmpty(skuInfoValue)) {
            return "未知错误";
        }
        //校验合法性
        SeckillSkuRedisTo redisTo = JSON.parseObject(String.valueOf(skuInfoValue),SeckillSkuRedisTo.class);
        Long starttime = redisTo.getStartTime();
        Long endtime = redisTo.getEndTime();
        long currentTime = System.currentTimeMillis();
        //判断当前这个秒杀请求是否在活动时间区间内(效验时间的合法性)
        if(currentTime >= starttime && currentTime <= endtime){
            //2.检验随机码
            String randomCode2  = redisTo.getRandomCode();
            String skuId = String.valueOf(redisTo.getSkuId());
            if(randomCode2.equals(randomCode) && skuId.equals(id)){
                //TODO 验证购买量和限制量是否一致
                //获取信号量
                String seckillCount = redisTemplate.opsForValue().get(SKU_STOCK_SEMAPHORE + randomCode2);
                assert seckillCount != null;
                Integer count = Integer.valueOf(seckillCount);
                if( count > 0){
                    //4.验证是否已经购买了（幂等性处理）,如果秒杀成功，就去占位
                    String key = userDTO.getId() + "_" + id;
                    //设置自动过期(活动结束时间-当前时间)
                    Long ttl = endtime - currentTime;
                    boolean isBuy = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "ok", ttl, TimeUnit.MILLISECONDS));
                    if(isBuy){
                        //占位成功说明从来没有买过,分布式锁(获取信号量-1)
                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                        boolean semaphoreCount = semaphore.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
                        if(semaphoreCount){
                            //快速创建订单
                            // 秒杀成功 快速下单 发送消息到 MQ
                            String snId = IdWorker.getTimeId();
                            SeckillOrderTo seckillOrderTo = new SeckillOrderTo();
                            seckillOrderTo.setOrderSn(snId);
                            seckillOrderTo.setMemberId(userDTO.getId());
                            seckillOrderTo.setNum(1);
                            seckillOrderTo.setPromotionSessionId(redisTo.getPromotionSessionId());
                            seckillOrderTo.setSeckillPrice(redisTo.getSeckillPrice());
                            seckillOrderTo.setSkuId(redisTo.getSkuId());
                            rabbitTemplate.convertAndSend("order-event-exchange","order.seckill.order",seckillOrderTo);
                            long s2 = System.currentTimeMillis();
                            log.info("耗时..." + (s2 - s1));
                            return snId;
                        }else {
                           return "";
                        }

                    }else {
                        return "3";
                    }
                }else{
                    return "2";
                }
            }else {
                return "1";
            }
        }
        else{
            return "0";
        }



    }

    /**
     * 缓存秒杀活动的商品信息
     * @param seckillProductVos
     */
    private void saveProductSkuInfo(List<SeckillProductVo> seckillProductVos) {
        //准备hash操作，绑定hash
        BoundHashOperations<String,Object,Object> operations = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);

        seckillProductVos.stream().forEach(item ->{
            //生成随机码
            String token = UUID.randomUUID().toString().replace("-","");
            if(!operations.hasKey(item.getSkuId().toString())){
                //缓存商品消息
                SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                Long id = item.getSkuId();
                //1.远程查询商品sku消息
                R skuInfo = productFeignService.getSkuInfo(id);
                if(skuInfo.getCode() == 0){
                    SkuInfoVo skuInfoVo = skuInfo.getData("sku" ,new TypeReference<SkuInfoVo>(){});
                    redisTo.setSkuInfo(skuInfoVo);
                }
                //2、sku的秒杀信息
                BeanUtils.copyProperties(item,redisTo);
                //3、设置当前商品的秒杀时间信息
                redisTo.setStartTime(item.getStartTime().getTime());
                redisTo.setEndTime(item.getEndTime().getTime());
                //4.设置商品的随机码（防止恶意攻击）
                redisTo.setRandomCode(token);
                //序列化json格式存入Redis中
                String seckillValue = JSON.toJSONString(redisTo);
                operations.put(item.getSkuId().toString(),seckillValue);

                //5、使用库存作为分布式Redisson信号量（限流）
                // 使用库存作为分布式信号量
                RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                //使用库存作为信号量的数量
                semaphore.trySetPermits(item.getSeckillCount());
            }



        });

    }
}
