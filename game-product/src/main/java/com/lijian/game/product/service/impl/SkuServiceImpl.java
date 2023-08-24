package com.lijian.game.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lijian.game.product.entity.*;
import com.lijian.game.product.service.*;
import com.lijian.game.product.vo.DataVo;
import com.lijian.game.product.vo.GameDetialVo;
import com.lijian.game.product.vo.LabelVo;
import com.lijian.game.product.vo.ListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.product.dao.SkuDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.print.DocFlavor;

import static com.lijian.game.product.utils.RedisConstants.CACHE_GAME_KEY;


@Service("skuService")
public class SkuServiceImpl extends ServiceImpl<SkuDao, SkuEntity> implements SkuService {
    @Resource
    SkuDao skuDao;
    @Resource
    SpuService spuService;
    @Resource
    BrandService brandService;
    @Resource
    SpuDetailService spuDetailService;
    @Resource
    SpuImagesService spuImagesService;
    @Resource
    private ThreadPoolExecutor executor;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuEntity> page = this.page(
                new Query<SkuEntity>().getPage(params),
                new QueryWrapper<SkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuEntity> search(Map<String, Object> params) {
       List<String> list = new ArrayList<>();
   for (Map.Entry<String,Object> entry : params.entrySet()){
       list.add(entry.getKey());
       list.add((String) entry.getValue());
   }
   Boolean isAsc = true;
   if(list.get(3).equals("asc")){
       isAsc = true;
   }else{
       isAsc = false;
   }

   if( "zh".equals(list.get(2)) ){
       return this.baseMapper.selectList(new QueryWrapper<SkuEntity>().
               like("title",list.get(1)));
   }else if( "price".equals(list.get(2))){
       return this.baseMapper.selectList(new QueryWrapper<SkuEntity>().
               like("title",list.get(1)).orderBy(true,isAsc,"price"));
   }else {
       return this.baseMapper.selectList(new QueryWrapper<SkuEntity>().
               like("title",list.get(1)));
   }


    }

    @Override
    public  List<ArrayList<List<ListVo>>> label() {
        //1.查询所有label存储起来
        QueryWrapper<SkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("labels").eq("spu_id", 2);
        SkuEntity skuEntities = (SkuEntity) this.baseMapper.selectOne(queryWrapper);
        String label = skuEntities.getLabels();
        String[] labels = label.split(",");
        //2.遍历label 查询出包含label关键词的游戏存储其信息
        LabelVo labelVo = new LabelVo();

       List<ArrayList<List<ListVo>>> dataVos = new ArrayList<>();
       List<List<ListVo>> listVos = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            List<SkuEntity> skuEntity = this.baseMapper.selectList(new QueryWrapper<SkuEntity>().like("labels", labels[i]));


            List<ListVo> vos = skuEntity.stream().map(skuEntity1 -> {

                ListVo listVo = new ListVo();
                listVo.setImgUrl(skuEntity1.getImages());
                listVo.setName(skuEntity1.getTitle());

                return listVo;
            }).collect(Collectors.toList());
            listVos.add( vos);
            List<DataVo> vos1 = listVos.stream().map(listVos1 -> {
                DataVo dataVo = new DataVo();

                dataVo.setList(Collections.singletonList(listVos1));
                return dataVo;
            }).collect(Collectors.toList());


        }
        dataVos.add((ArrayList<List<ListVo>>) listVos);




        return dataVos;
    }
    /**
     * 每一个需要缓存的数据我们都来指定要放到那个名字的缓存。【缓存的分区(按照业务类型分)】
     * 代表当前方法的结果需要缓存，如果缓存中有，方法都不用调用，如果缓存中没有，会调用方法。最后将方法的结果放入缓存
     * 默认行为
     *      如果缓存中有，方法不再调用
     *      key是默认生成的:缓存的名字::SimpleKey::[](自动生成key值)
     *      缓存的value值，默认使用jdk序列化机制，将序列化的数据存到redis中
     *      默认时间是 -1：
     *
     *   自定义操作：key的生成
     *      指定生成缓存的key：key属性指定，接收一个Spel
     *      指定缓存的数据的存活时间:配置文档中修改存活时间
     *      将数据保存为json格式
     *
     *
     * 4、Spring-Cache的不足之处：
     *  1）、读模式
     *      缓存穿透：查询一个null数据。解决方案：缓存空数据
     *      缓存击穿：大量并发进来同时查询一个正好过期的数据。解决方案：加锁 ? 默认是无加锁的;使用sync = true来解决击穿问题
     *      缓存雪崩：大量的key同时过期。解决：加随机时间。加上过期时间
     *  2)、写模式：（缓存与数据库一致）
     *      1）、读写加锁。
     *      2）、引入Canal,感知到MySQL的更新去更新Redis
     *      3）、读多写多，直接去数据库查询就行
     *
     *  总结：
     *      常规数据（读多写少，即时性，一致性要求不高的数据，完全可以使用Spring-Cache）：写模式(只要缓存的数据有过期时间就足够了)
     *      特殊数据：特殊设计
     *
     *  原理：
     *      CacheManager(RedisCacheManager)->Cache(RedisCache)->Cache负责缓存的读写
     * @return
     */
    @Cacheable(value = "spu",key = "#root.methodName",sync = true)
    @Override
    public List<SkuEntity> like() {
      return  this.baseMapper.selectList(new QueryWrapper<SkuEntity>().orderByDesc("spu_id").last("limit 4"));

    }

    @Override
    public List<GameDetialVo> gameDetailById(Long id) throws ExecutionException, InterruptedException{
        //1、占分布式锁。去redis占坑      设置过期时间必须和加锁是同步的，保证原子性（避免死锁）
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock",uuid);
        if(lock){

            System.out.println("获取分布式锁成功，开始执行业务...");
            List<GameDetialVo> gameDetialVoList = null;
            try {
                //加锁成功开始执行业务
                gameDetialVoList = dataFromDb(id);
            }finally {
                //先去redis查询下保证当前的锁是自己的
                //获取值对比，对比成功删除=原子性 lua脚本解锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                //删除锁
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return dataFromDb(id);
        } else {
            System.out.println("获取分布式锁失败等待重试...");
            //加锁失败...重试机制
            //休眠一百毫秒
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch (InterruptedException e){e.printStackTrace();}
            return gameDetailById(id);
        }


    }

    private List<GameDetialVo> dataFromDb(Long id) throws InterruptedException, ExecutionException {
        //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
        String Json = stringRedisTemplate.opsForValue().get(CACHE_GAME_KEY+id);
        if (!org.springframework.util.StringUtils.isEmpty(Json)) {
            //缓存不为空直接返回
            return JSON.parseObject(Json, new TypeReference< List<GameDetialVo>>() {
            });
        }

        GameDetialVo gameDetialVo = new GameDetialVo();
        CompletableFuture<SkuEntity> completableFuture = CompletableFuture.supplyAsync(() ->{
            //1.获取sku信息
            SkuEntity skuEntity = this.getById(id);
            BeanUtils.copyProperties(skuEntity,gameDetialVo);
            return skuEntity;
        },executor);

        CompletableFuture<Void> completableFuture1 = completableFuture.thenAcceptAsync((res) ->{
            //2.获取品牌信息
            SpuEntity spuEntity = spuService.getById(res.getSpuId());
            BrandEntity brandEntity = brandService.getById(spuEntity.getBrandId());
            gameDetialVo.setBrandName(brandEntity.getName());
            gameDetialVo.setBrandImage(brandEntity.getImage());
            gameDetialVo.setSubTitle(spuEntity.getSubTitle());
        },executor);

        CompletableFuture<Void> completableFuture2 = completableFuture.thenAcceptAsync((res) ->{
            //3.获取详细信息
            SpuDetailEntity spuDetailEntity = spuDetailService.getById(res.getSpuId());
            BeanUtils.copyProperties(spuDetailEntity,gameDetialVo);
        },executor);

        CompletableFuture<Void> completableFuture3 = completableFuture.thenAcceptAsync((res) ->{
            //4.获取图集
            List<SpuImagesEntity> spuImagesEntityList = spuImagesService
                    .list(new QueryWrapper<SpuImagesEntity>().eq("spu_id",res.getSpuId()));
            List<List<String>> collect = spuImagesEntityList.stream().map(spuImagesEntity -> {
                List<String> strings = new ArrayList<>();
                strings.add(spuImagesEntity.getImgUrl());
                return strings;
            }).collect(Collectors.toList());
            gameDetialVo.setImages(collect);
        },executor);

        //等到所有任务都完成
        CompletableFuture.allOf(completableFuture1,completableFuture1,completableFuture2,completableFuture3).get();
        List<GameDetialVo> gameDetialVoList = new ArrayList<>();
        gameDetialVoList.add(gameDetialVo);
        //3、将查到的数据放入缓存
        String valueJson = JSON.toJSONString(gameDetialVoList);
        stringRedisTemplate.opsForValue().set(CACHE_GAME_KEY+id, valueJson, 1, TimeUnit.DAYS);
        return gameDetialVoList;
    }
}
