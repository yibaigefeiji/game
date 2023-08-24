package com.lijian.game.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.to.SeckillOrderTo;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.order.DTO.OrderConfirmDto;
import com.lijian.game.order.entity.OrderEntity;
import com.lijian.game.order.entity.OrderItemEntity;
import com.lijian.game.order.vo.OrderSubmitVo;
import com.lijian.game.order.vo.PayAsyncVo;
import com.lijian.game.order.vo.PayVo;
import com.lijian.game.order.vo.SubmitOrderResponseVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 12:54:09
 */
@Service
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 订单详情页数据
     */
    OrderConfirmDto getDetail() throws ExecutionException, InterruptedException;
    /**
     * 下单功能
     * @param
     * @return
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);
    /**
     * 按照订单号获取订单信息
     * @param orderSn
     * @return
     */
    OrderEntity getOrderByOrderSn(String orderSn);
    /**
     * 关闭订单
     * @param orderEntity
     */
    void closeOrder(OrderEntity orderEntity);
    /**
     * 获取当前订单的支付信息
     * @param orderSn
     * @return
     */
    PayVo getOrderPay(String orderSn);
    /**
     * 根据id获取订单号（数据库最新的一条）
     * @param id
     * @return
     */
    OrderEntity getOrderById(Long id);
    /**
     *支付宝异步通知处理订单数据
     * @param asyncVo
     * @return
     */
    String handlePayResult(PayAsyncVo asyncVo);
    /**
     * 创建秒杀单
     * @param orderTo
     */
    void createSeckillOrder(SeckillOrderTo orderTo);
}

