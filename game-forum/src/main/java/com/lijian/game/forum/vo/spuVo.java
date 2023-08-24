package com.lijian.game.forum.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lenovo
 */
@Data
public class spuVo {
    private Long id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 副标题 (一般是促销信息)
     */
    private String subTitle;
    /**
     * 开发商id (商品所属的品牌)
     */
    private Long brandId;
    /**
     * 是否上架 (0-下架，1-上架)
     */
    private Integer saleable;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date updateTime;
}
