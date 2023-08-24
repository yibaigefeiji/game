package com.lijian.game.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lenovo
 */
@Data
public class GameDetialVo {
    private Long spuId;
    private String title;
    private String labels;
    private Long price;
    /**
     * 副标题 (一般是促销信息)
     */
    private String subTitle;
    private Long brandId;
    private String brandName;
    private String brandImage;
    /**
     * 商品描述信息
     */
    private String description;
    /**
     * 支持中文
     */
    private String supportChinese;
    /**
     * 支持人数
     */
    private String supportNumber;
    /**
     * 手柄
     */
    private String supportHandle;
    /**
     * 支持多人模式
     */
    private String supportMultiPersonMode;
    /**
     * 远程游戏
     */
    private String supportRemote;
    /**
     * 包含游戏内购
     */
    private String supportPurchase;

    private  List<List<String>> images;
}
