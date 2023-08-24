package com.lijian.game.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.ware.entity.WareOrderTaskEntity;
import org.springframework.stereotype.Service;


import java.util.Map;

/**
 * 库存工作单
 *
 * @author lijian
 */
@Service
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

