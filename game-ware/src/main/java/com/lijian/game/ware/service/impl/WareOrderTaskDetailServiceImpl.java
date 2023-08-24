package com.lijian.game.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;
import com.lijian.game.ware.dao.WareOrderTaskDetailDao;
import com.lijian.game.ware.entity.WareOrderTaskDetailEntity;
import com.lijian.game.ware.service.WareOrderTaskDetailService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


/**
 * @author lijian
 */
@Service("wareOrderTaskDetailService")
public class WareOrderTaskDetailServiceImpl extends ServiceImpl<WareOrderTaskDetailDao, WareOrderTaskDetailEntity> implements WareOrderTaskDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskDetailEntity> page = this.page(
                new Query<WareOrderTaskDetailEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskDetailEntity>()
        );

        return new PageUtils(page);
    }


}
