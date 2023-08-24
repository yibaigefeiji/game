package com.lijian.game.ware.dao;

import com.lijian.game.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 13:18:00
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Long getSkuStock(@Param("skuId") Long skuId);
    /**
     * 锁定库存
     * @param skuId
     * @param count
     * @return
     */
    Long lockSkuStock(@Param("skuId")Long skuId, @Param("count")Integer count);
    /**
     * 解锁库存
     */
    void unLockStock(@Param("skuId")Long skuId,  @Param("skuNum")Integer skuNum);
}
