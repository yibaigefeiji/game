package com.lijian.game.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.product.dao.SpuDetailDao;
import com.lijian.game.product.entity.SpuDetailEntity;
import com.lijian.game.product.service.SpuDetailService;


@Service("spuDetailService")
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailDao, SpuDetailEntity> implements SpuDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuDetailEntity> page = this.page(
                new Query<SpuDetailEntity>().getPage(params),
                new QueryWrapper<SpuDetailEntity>()
        );

        return new PageUtils(page);
    }

}
