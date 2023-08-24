package com.lijian.game.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-30 20:44
 **/

@Data
public class SkuInfoVo {

    private Long id;
    /**
     * spuId
     */
    private Long spuId;
    /**
     * sku名称
     */
    private String skuName;
    /**
     * sku介绍描述
     */
    private String skuDesc;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 商品图片 (多个图片用,号分割)
     */
    private String images;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String skuSubtitle;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 销量
     */
    private Long saleCount;

}
