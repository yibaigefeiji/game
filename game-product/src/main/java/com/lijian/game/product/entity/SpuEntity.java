package com.lijian.game.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu表描述的是一个抽象性的商品，比如 iphone8
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:10
 */
@Data
@TableName("t_spu")
public class SpuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SPU Id
	 */
	@TableId
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
