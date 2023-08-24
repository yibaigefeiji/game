package com.lijian.game.user.dao;

import com.lijian.game.user.entity.SeckillEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动商品
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-28 09:29:31
 */
@Mapper
public interface SeckillDao extends BaseMapper<SeckillEntity> {
	
}
