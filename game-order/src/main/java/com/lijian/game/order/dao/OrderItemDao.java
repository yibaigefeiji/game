package com.lijian.game.order.dao;

import com.lijian.game.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 12:54:09
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
