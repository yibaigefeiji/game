package com.lijian.game.user.entity;

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
 * @date 2023-02-27 13:35:28
 */
@Data
@TableName("t_follow")
public class FollowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long userId;
	/**
	 * 
	 */
	private Long followId;
	/**
	 * 
	 */
	private Date creatTime;

}
