package com.lijian.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.user.entity.LikeSpuEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户收藏的商品
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Service
public interface LikeSpuService extends IService<LikeSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

