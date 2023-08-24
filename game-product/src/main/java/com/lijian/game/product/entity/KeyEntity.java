package com.lijian.game.product.entity;

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
 * @date 2022-09-25 10:52:11
 */
@Data
@TableName("t_key")
public class KeyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long skuId;
	/**
	 * 
	 */
	private String key;

}
