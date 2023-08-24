package com.lijian.game.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijian.game.common.exception.NoStockException;
import com.lijian.game.common.vo.KeyVo;
import com.lijian.game.common.vo.OrderDetailVo;
import com.lijian.game.order.DTO.OrderConfirmDto;
import com.lijian.game.order.entity.OrderItemEntity;
import com.lijian.game.order.feign.ProductFeignService;
import com.lijian.game.order.service.OrderItemService;
import com.lijian.game.order.vo.OrderSubmitVo;
import com.lijian.game.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.order.entity.OrderEntity;
import com.lijian.game.order.service.OrderService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;


/**
 * 订单
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 12:54:09
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductFeignService productFeignService;
    /**
     * 根据用户id查询当前支付成功的订单(远程调用）
     * @param id
     * @return
     */
    @GetMapping(value = "/orderItem/{id}")
   public List<OrderDetailVo> getOrderItem(@PathVariable("id") Long id){
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
     * 根据订单编号查询订单状态(远程调用）
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn) {
        OrderEntity orderEntity = orderService.getOrderByOrderSn(orderSn);
        return R.ok().setData(orderEntity);
    }


    /**
     * 订单详情页数据
     */
    @RequestMapping("/detail")
    public OrderConfirmDto detail() throws ExecutionException, InterruptedException {
        return orderService.getDetail();
    }
    /**
    * 下单功能
    * @param
    * @return
    */
    @PostMapping(value = "/submitOrder")
    public R submitOrder(OrderSubmitVo vo){
       try {
           SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
           //下单成功来到支付选择页
           //下单失败回到订单确认页重新确定订单信息
           if (responseVo.getCode() == 0) {
               //成功

               return R.ok().setData(responseVo.getOrder().getOrderSn());
           } else {
               String msg = "";
               switch (responseVo.getCode()) {
                   //令牌订单信息过期，请刷新再次提交
                   case 1: msg += "1"; break;
                   //订单商品价格发生变化，请确认后再次提交
                   case 2: msg += "2"; break;
                   //库存锁定失败，商品库存不足
                   case 3: msg += "3"; break;
               }

               return R.error().setData(msg);
           }
       }catch (Exception e){
           if (e instanceof NoStockException) {
               String message = ((NoStockException)e).getMessage();

           }
           return R.error().setData("fail");
       }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
