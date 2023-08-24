package com.lijian.game.product.dao;

import com.lijian.game.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开发商，一个开发商下有多个游戏（spu），一对多关系
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:10
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
	
}
