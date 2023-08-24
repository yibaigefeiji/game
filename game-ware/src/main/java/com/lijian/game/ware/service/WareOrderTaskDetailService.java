package com.lijian.game.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.ware.entity.WareOrderTaskDetailEntity;
import org.springframework.stereotype.Service;


import java.util.Map;

/**
 * 库存工作单
 *

 */
@Service
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

