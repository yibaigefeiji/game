package com.lijian.game.user.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.user.dao.LikeSpuDao;
import com.lijian.game.user.entity.LikeSpuEntity;
import com.lijian.game.user.service.LikeSpuService;


@Service("likeSpuService")
public class LikeSpuServiceImpl extends ServiceImpl<LikeSpuDao, LikeSpuEntity> implements LikeSpuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LikeSpuEntity> page = this.page(
                new Query<LikeSpuEntity>().getPage(params),
                new QueryWrapper<LikeSpuEntity>()
        );

        return new PageUtils(page);
    }

}
