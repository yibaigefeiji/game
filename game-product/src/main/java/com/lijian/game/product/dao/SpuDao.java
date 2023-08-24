package com.lijian.game.product.dao;

import com.lijian.game.product.entity.SpuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * spu表描述的是一个抽象性的商品，比如 iphone8
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:10
 */
@Mapper
public interface SpuDao extends BaseMapper<SpuEntity> {
	
}
