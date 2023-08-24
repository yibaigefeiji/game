package com.lijian.game.product.dao;

import com.lijian.game.product.entity.SkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@Mapper
public interface SkuDao extends BaseMapper<SkuEntity> {
	
}
