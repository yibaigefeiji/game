package com.lijian.game.order.dao;

import com.lijian.game.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 12:54:09
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {



    OrderEntity getNewOrder(@Param("id")Long id);

    void updateOrderStatus(@Param("orderSn") String orderSn,
                           @Param("code") Integer code,
                           @Param("payType") Integer payType);
}
