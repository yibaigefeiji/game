package com.lijian.game.product.dao;

import com.lijian.game.product.entity.KeyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-25 10:52:11
 */
@Mapper
public interface KeyDao extends BaseMapper<KeyEntity> {

    KeyEntity getBySku(@Param("skuId")Long skuId);
}
