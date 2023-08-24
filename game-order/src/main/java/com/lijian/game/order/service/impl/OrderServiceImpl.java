package com.lijian.game.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.lijian.game.common.exception.NoStockException;
import com.lijian.game.common.to.OrderTo;
import com.lijian.game.common.to.SeckillOrderTo;
import com.lijian.game.common.utils.R;
import com.lijian.game.common.vo.KeyVo;
import com.lijian.game.common.vo.OrderDetailVo;
import com.lijian.game.order.DTO.OrderConfirmDto;
import com.lijian.game.order.DTO.OrderItemDto;
import com.lijian.game.order.DTO.UserDTO;
import com.lijian.game.order.constant.PayConstant;
import com.lijian.game.order.entity.OrderItemEntity;
import com.lijian.game.order.enume.OrderStatusEnum;
import com.lijian.game.order.feign.CartFeignService;
import com.lijian.game.order.feign.ProductFeignService;
import com.lijian.game.order.feign.WareFeignService;
import com.lijian.game.order.service.OrderItemService;
import com.lijian.game.order.to.OrderCreateTo;
import com.lijian.game.order.to.SpuInfoVo;
import com.lijian.game.order.utils.UserHolder;
import com.lijian.game.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

import com.lijian.game.order.dao.OrderDao;
import com.lijian.game.order.entity.OrderEntity;
import com.lijian.game.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;

import static com.lijian.game.order.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;

@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    private ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Resource
    ProductFeignService productFeignService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmDto getDetail() throws ExecutionException, InterruptedException {
        //构建OrderItemEntity
        OrderConfirmDto confirmVo = new OrderConfirmDto();
        //获取当前用户登录信息
        UserDTO userDTO = UserHolder.getUser();
        extracted(confirmVo, userDTO);

        return confirmVo;
    }
    /**
     * 下单功能
     * @param
     * @return
     */
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        confirmVoThreadLocal.set(vo);

        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        //去创建，下订单，验证令牌，锁库存...

        //获取当前用户信息
        UserDTO userDTO = UserHolder.getUser();
        responseVo.setCode(0);
        //1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();
        //通过lua脚本原子性验证令牌和删除令牌
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script,Long.class),
                Arrays.asList(USER_ORDER_TOKEN_PREFIX + userDTO.getId()),
                orderToken);
        if(result == 0L){
            //令牌验证失败
            responseVo.setCode(1);
            return  responseVo;
        } else {
            //令牌验证成功
            //1、创建订单、订单项等信息
            OrderCreateTo order = createOrder();
            //2、验证价格
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            //金额对比
            log.info("******开始对比金额******");
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                    //3.保存订单
                saveOrder(order);
                log.info("******开始准备锁库存所需信息******");

                //4、库存锁定,只要有异常，回滚订单数据
                WareSkuLockVo lockVo = new WareSkuLockVo();
                lockVo.setOrderSn(order.getOrder().getOrderSn());
                //获取需要所库存的商品信息
                List<OrderItemDto> orderItemVos = order.getOrderItems().stream().map(item -> {
                    OrderItemDto orderItemDto = new OrderItemDto();
                    orderItemDto.setSkuId(item.getSkuId());
                    orderItemDto.setTitle(item.getSkuName());
                    orderItemDto.setCount(item.getSkuQuantity());
                    orderItemDto.setPrice(item.getSkuPrice());
                    return orderItemDto;
                }).collect(Collectors.toList());
                lockVo.setLocks(orderItemVos);
                log.info("******开始执行锁库存操作******");
                R r = wareFeignService.orderLockStock(lockVo);
                if(r.getCode() == 0){
                    //锁定库存成功
                    responseVo.setOrder(order.getOrder());
                    //模拟其他服务出现错误，订单回滚，库存不回滚
//                    int i = 10/0;
                    //订单创建成功，给mq发送消息
                    rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order.getOrder());
                    return responseVo;
                }else{
                    //锁定库存失败
                    String msg = (String) r.get("msg");
                    System.out.println("msg:   "+msg);
                    throw new NoStockException(msg);
                }


            }else {
                responseVo.setCode(2);
                return responseVo;
            }
        }

    }
    /**
     * 按照订单号获取订单信息
     * @param orderSn
     * @return
     */
    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        return this.baseMapper.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
    }
    /**
     * 关闭订单
     * @param orderEntity
     */
    @Override
    public void closeOrder(OrderEntity orderEntity) {
        //查询订单当前状态
        OrderEntity order = this.getById(orderEntity.getId());
        if(order.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())){
            //关单
            OrderEntity update = new OrderEntity();
            update.setId(orderEntity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);
            //发给mq消息进行解锁库存
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(order,orderTo);
            rabbitTemplate.convertAndSend("order-event-exchange","order.release.other",orderTo);

        }

    }

    /**
     * 获取当前订单的支付信息
     * @param orderSn
     * @return
     */
    @Override
    public PayVo getOrderPay(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity orderInfo = this.getOrderByOrderSn(orderSn);
        //保留两位小数点，向上取值
        BigDecimal payAmount = orderInfo.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(payAmount.toString());
        payVo.setOut_trade_no(orderInfo.getOrderSn());
        //查询订单项的数据
        List<OrderItemEntity> orderItemInfo = orderItemService.list(
                new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        OrderItemEntity orderItemEntity = orderItemInfo.get(0);
        payVo.setBody(orderItemEntity.getSpuName());
        payVo.setSubject(orderItemEntity.getSkuName());
        return payVo;
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        OrderEntity orderEntity = orderDao.getNewOrder(id);

        return orderEntity;
    }

    @Override
    public String handlePayResult(PayAsyncVo asyncVo) {
        //获取当前状态
        String tradeStatus = asyncVo.getTrade_status();
        if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
            //支付成功状态
            String orderSn = asyncVo.getOut_trade_no(); //获取订单号
            this.updateOrderStatus(orderSn,OrderStatusEnum.PAYED.getCode(), PayConstant.ALIPAY);
            //解锁购物车状态
//            this.updateCart();
            UserDTO userDTO = UserHolder.getUser();
            int id = 2;
            BoundHashOperations<String,Object,Object> operations = redisTemplate.boundHashOps("game:cart:2");
            List<OrderDetailVo> OrderDetailVos = new ArrayList<>();
            OrderDetailVos = getOrderItem((long) id);
            System.out.println("dasf:             "+OrderDetailVos);
            for (int i = 0; i < OrderDetailVos.size(); i++) {
                operations.delete(OrderDetailVos.get(i).getSkuId().toString());
            }


        }
        return null;
    }

    private List<OrderDetailVo> getOrderItem(Long id) {
        OrderEntity orderEntity = orderService.getOrderById(id);
        String orderSn =  orderEntity.getOrderSn();
        List<OrderItemEntity> orderItemEntity = orderItemService
                .list(new QueryWrapper<OrderItemEntity>().eq("order_sn",orderSn));
        return orderItemEntity.stream().map(item -> {
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            orderDetailVo.setOrderSn(item.getOrderSn());
            orderDetailVo.setSkuId(item.getSkuId());
            orderDetailVo.setSkuName(item.getSkuName());
            orderDetailVo.setSkuPic(item.getSkuPic());
            orderDetailVo.setSkuPrice(item.getSkuPrice());
            orderDetailVo.setSkuQuantity(item.getSkuQuantity());
            KeyVo keyVo = productFeignService.getKeyInfoBySkuId(item.getSkuId());
            orderDetailVo.setKey(keyVo.getKey());
            return orderDetailVo;
        }).collect(Collectors.toList());
    }

    /**
     * 创建秒杀单
     * @param orderTo
     */
    @Override
    public void createSeckillOrder(SeckillOrderTo orderTo) {
        //TODO 保存订单信息
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderTo.getOrderSn());
        orderEntity.setMemberId(orderTo.getMemberId());
        orderEntity.setCreateTime(new Date());
        BigDecimal totalPrice = orderTo.getSeckillPrice().multiply(BigDecimal.valueOf(orderTo.getNum()));
        orderEntity.setPayAmount(totalPrice);
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        //保存订单
        this.save(orderEntity);
        //保存订单项信息
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrderSn(orderTo.getOrderSn());
        orderItem.setRealAmount(totalPrice);
        orderItem.setSkuQuantity(orderTo.getNum());
        //保存商品的sku信息
        R r = productFeignService.info(orderTo.getSkuId());
        SkuInfoVo skuInfoVo = r.getData("sku",new TypeReference<SkuInfoVo>(){});
        orderItem.setOrderSn(orderTo.getOrderSn());
        orderItem.setSkuPic(skuInfoVo.getImages());
        orderItem.setSkuId(orderTo.getSkuId());
        orderItem.setSkuName(skuInfoVo.getTitle());
        orderItem.setSkuQuantity(orderTo.getNum());
        orderItem.setRealAmount(orderTo.getSeckillPrice());

        //保存订单项数据
        orderItemService.save(orderItem);


    }

    /**
     * 修改订单状态
     * @param orderSn
     * @param code
     */
    private void updateOrderStatus(String orderSn, Integer code, Integer payType) {
        this.baseMapper.updateOrderStatus(orderSn,code,payType);
    }

    /**
     * 保存订单所有数据
     * @param order
     */
    private void saveOrder(OrderCreateTo order) {
        //获取订单信息
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        orderEntity.setCreateTime(new Date());
        log.info("******开始保存订单******");


        //保存订单
        this.baseMapper.insert(orderEntity);
        //获取订单项信息
        List<OrderItemEntity> orderItemEntities = order.getOrderItems();
        log.info("******开始保存订单项******");

        //批量保存订单项信息
        orderItemService.saveBatch(orderItemEntities);
        log.info("******所有订单项已经保存成功******");


    }

    /**
     * 生成订单信息（包扩订单信息，订单项信息，验价）
     * @return
     */
    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();
        //1.生成订单号创建订单
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = builderOrder(orderSn);
        //2.获取到所有的订单项信息
        List<OrderItemEntity> orderItemEntities = builderOrderItems(orderSn);
        //3、验价(计算价格)
        computePrice(orderEntity,orderItemEntities);

        createTo.setOrder(orderEntity);
        createTo.setOrderItems(orderItemEntities);

        return createTo;
    }
    /**
     * 计算价格的方法
     * @param orderEntity
     * @param orderItemEntities
     */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        //总价
        BigDecimal total = new BigDecimal("0.0");
        //优惠价
        BigDecimal coupon = new BigDecimal("0.0");

        //订单总额，叠加每一个订单项的总额信息
        for (OrderItemEntity orderItem : orderItemEntities) {
            //优惠价格信息
            coupon = coupon.add(orderItem.getCouponAmount());
            //总价
            total = total.add(orderItem.getRealAmount());

        }
        orderEntity.setTotalAmount(total);
        orderEntity.setPayAmount(total);
    }

    /**
     * 构建所有订单项数据
     * @return
     */
    private List<OrderItemEntity> builderOrderItems(String orderSn) {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        //最后确定每个购物项的价格
        List<CartItemVo> currentCartItems = cartFeignService.getCurrentCartItem();
        if(currentCartItems != null && currentCartItems.size() > 0){
            orderItemEntityList = currentCartItems.stream().map(item ->{
                //构建订单向数据
                OrderItemEntity orderItemEntity = builderOrderItem(item);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;
            }).collect(Collectors.toList());
        }
        return orderItemEntityList;
    }
    /**
     * 构建某一个订单项的数据
     * @param item
     * @return
     */
    private OrderItemEntity builderOrderItem(CartItemVo item) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        //1、商品的spu信息
        Long skuId = item.getSkuId();
        //获取spu的信息
        R spuInfo = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoVo = spuInfo.getData("data",new TypeReference<SpuInfoVo>(){});
        orderItemEntity.setSpuId(spuInfoVo.getId());
        orderItemEntity.setSpuName(spuInfoVo.getName());
        orderItemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
        //2、商品的sku信息
        orderItemEntity.setSkuId(skuId);
        orderItemEntity.setSkuName(item.getTitle());
        orderItemEntity.setSkuPrice(item.getPrice());
        orderItemEntity.setSkuPic(item.getImage());
        orderItemEntity.setSkuQuantity(item.getCount());
        //TODO:3、商品的优惠信息

        //4、订单项的价格信息
        orderItemEntity.setPromotionAmount(BigDecimal.ZERO);
        orderItemEntity.setCouponAmount(BigDecimal.ZERO);
        //当前订单项的实际金额.总额 - 各种优惠价格
        //原来的价格
        BigDecimal origin = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        //原价减去优惠价得到最终的价格
        BigDecimal subtract = origin.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount());
        orderItemEntity.setRealAmount(subtract);

        return orderItemEntity;
    }

    /**
     * 构建订单数据
     * @param orderSn
     * @return
     */
    private OrderEntity builderOrder(String orderSn) {
        //获取当前用户。
        UserDTO userDTO = UserHolder.getUser();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMemberId(userDTO.getId());
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberUsername(userDTO.getNickName());
       orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
       return orderEntity;

    }

    private void extracted(OrderConfirmDto confirmVo, UserDTO userDTO) throws InterruptedException, ExecutionException {
        //TODO :获取当前线程请求头信息(解决Feign异步调用丢失请求头问题)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //开启第一个异步任务
        CompletableFuture<Void> cartInfoFuture = CompletableFuture.runAsync(()->{
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1、远程查询购物车所有选中的购物项
            List<CartItemVo> currentCartItems = cartFeignService.getCurrentCartItem();
            confirmVo.setItems(currentCartItems);
        },threadPoolExecutor).thenRunAsync(()->{
            List<CartItemVo> items = confirmVo.getItems();
            //获取全部商品的id
            List<Long> skuIds = items.stream()
                    .map(CartItemVo::getSkuId)
                    .collect(Collectors.toList());

            //远程查询商品库存信息
            R skuHasStock = wareFeignService.getSkuHasStock(skuIds);
            List<SkuStockVo> skuStockVos = skuHasStock.getData("data",new TypeReference<List<SkuStockVo>>() {});
            if(skuStockVos != null && skuStockVos.size() > 0){
                //将skuStockVos集合转换为map
                Map<Long, Boolean> skuHasStockMap = skuStockVos.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                confirmVo.setStocks(skuHasStockMap);
            }

            //为用户设置一个token，三十分钟过期时间（存在redis）
            String token = UUID.randomUUID().toString().replace("-", "");
            redisTemplate.opsForValue().set(USER_ORDER_TOKEN_PREFIX+ userDTO.getId(),token,30, TimeUnit.MINUTES);
            confirmVo.setOrderToken(token);
        },threadPoolExecutor);
        CompletableFuture.allOf(cartInfoFuture).get();
    }

}
