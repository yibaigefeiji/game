package com.lijian.game.user.dao;

import com.lijian.game.user.entity.LikeSpuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户收藏的商品
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Mapper
public interface LikeSpuDao extends BaseMapper<LikeSpuEntity> {

}
