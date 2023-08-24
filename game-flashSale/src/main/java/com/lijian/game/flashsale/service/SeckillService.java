package com.lijian.game.flashsale.service;



import com.lijian.game.flashsale.to.SeckillSkuRedisTo;

import java.util.List;


/**
 * @author lijian
 */
public interface SeckillService {


    /**
     * 上架三天需要秒杀的商品
     */
    void uploadSeckillSku3Days();
    /**
     * 当前时间可以参与秒杀的商品信息
     * @return
     */
    List<SeckillSkuRedisTo> getCurrentSeckillProducts();
    /**
     * 秒杀商品
     */
    String kill(String id, String randomCode) throws InterruptedException;
}
