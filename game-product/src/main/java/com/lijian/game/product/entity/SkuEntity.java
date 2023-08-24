package com.lijian.game.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@Data
@TableName("t_sku")
public class SkuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SKU Id
	 */
	@TableId
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
