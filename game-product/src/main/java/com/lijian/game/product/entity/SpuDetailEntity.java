package com.lijian.game.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@Data
@TableName("t_spu_detail")
public class SpuDetailEntity implements Serializable {


	/**
	 * SPU Id
	 */
	@TableId(type = IdType.INPUT)
	private Long spuId;
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
	/**
	 *
	 */
	private Date createTime;
	/**
	 *
	 */
	private Date updateTime;

}
