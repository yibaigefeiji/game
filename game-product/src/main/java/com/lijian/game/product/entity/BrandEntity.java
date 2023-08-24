package com.lijian.game.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 开发商，一个开发商下有多个游戏（spu），一对多关系
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:10
 */
@Data
@TableName("t_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 开发商id
	 */
	@TableId
	private Long id;
	/**
	 * 开发商名称
	 */
	private String name;
	/**
	 * 开发商图片地址
	 */
	private String image;
	/**
	 * 开发商的首字母
	 */
	private String letter;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

}
