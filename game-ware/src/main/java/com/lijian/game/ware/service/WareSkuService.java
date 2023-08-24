package com.lijian.game.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.to.OrderTo;
import com.lijian.game.common.to.mq.StockLockedTo;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.ware.entity.WareSkuEntity;
import com.lijian.game.ware.vo.SkuHasStockVo;
import com.lijian.game.ware.vo.WareSkuLockVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 13:18:00
 */
@Service
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 查询sku是否有库存(供远程调用)
     * @return
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
    /**
     * 锁定库存
     * @param
     */
    boolean orderLockStock(WareSkuLockVo vo);
    /**
     * 解锁库存
     * @param to
     */
    void unlockStock(StockLockedTo to);
    /**
     * 解锁订单
     * @param orderTo
     */
    void unlockStock(OrderTo orderTo);
}

