package com.lijian.game.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lenovo
 */
@Data
public class OrderDetailVo {

    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 商品sku编号
     */
    private Long skuId;
    /**
     * 商品sku名字
     */
    private String skuName;
    /**
     * 商品sku图片
     */
    private String skuPic;
    /**
     * 商品sku价格
     */
    private BigDecimal skuPrice;
    /**
     * 商品购买的数量
     */
    private Integer skuQuantity;
    /**
     * key
     */
    private String key;
}
