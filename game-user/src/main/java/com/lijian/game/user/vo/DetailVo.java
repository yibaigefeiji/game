package com.lijian.game.user.vo;

import lombok.Data;

import java.util.Date;
@Data
public class DetailVo {
    private Long id;
    /**
     *
     */
    private String title;
    /**
     *
     */
    private String text;
    /**
     *
     */
    private String images;
    /**
     *
     */
    private Long userInfo;
    /**
     *
     */
    private String comment;
    /**
     *
     */
    private String likes;
    /**
     *
     */
    private Date time;
    /**
     *
     */
    private String lable;
    /**
     *
     */
    private String gameName;
    /**
     *
     */
    private Long gameSpu;

    private String nickName;

    private String icon;


    /**
     * 商品标题
     */
    private String titleP;
    /**
     * 商品图片 (多个图片用,号分割)
     */
    private String imagesP;
    /**
     * 商品标签 (多个标签用,号分割)
     */
    private String labels;
    /**
     * 销售价格 (单位为分)
     */
    private Long price;
}
