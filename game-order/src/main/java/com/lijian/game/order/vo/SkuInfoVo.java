package com.lijian.game.order.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author lenovo
 */
@Data
public class SkuInfoVo {
    /**
     * SKU Id
     */

    private Long id;
    /**
     * SPU Id
     */
    private Long spuId;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品图片 (多个图片用,号分割)
     */
    private String images;
    /**
     * 商品标签 (多个标签用,号分割)
     */
    private String labels;
    /**
     * 销售价格 (单位为分)
     */
    private Long price;
    /**
     * 是否有效 (0-无效，1-有效)
     */
    private Integer enable;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date updateTime;
}
