package com.lijian.game.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@TableName("t_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 密码，加密存储
     */
    private String password;
    /**
     * 昵称，默认是用户id
     */
    private String nickName;
    /**
     * 人物头像
     */
    private String icon;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
