package com.lijian.game.forum.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2023-02-23 21:16:47
 */
@Data
@TableName("t_forum")
public class ForumEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
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
	private Integer comment;
	/**
	 *
	 */
	private Integer likes;
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

	/**
	 * 是否被点赞过了
	 */
	@TableField(exist = false)
	private Boolean isLike;


}
