package com.lijian.game.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.product.dao.KeyDao;
import com.lijian.game.product.entity.KeyEntity;
import com.lijian.game.product.service.KeyService;


@Service("keyService")
public class KeyServiceImpl extends ServiceImpl<KeyDao, KeyEntity> implements KeyService {

    @Autowired
    private KeyDao keyDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<KeyEntity> page = this.page(
                new Query<KeyEntity>().getPage(params),
                new QueryWrapper<KeyEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public KeyEntity getBySku(Long skuId) {
        return keyDao.getBySku(skuId);
    }

}
