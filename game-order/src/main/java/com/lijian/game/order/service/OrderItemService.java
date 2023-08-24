package com.lijian.game.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.order.entity.OrderItemEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 订单项信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 12:54:09
 */
@Service
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

