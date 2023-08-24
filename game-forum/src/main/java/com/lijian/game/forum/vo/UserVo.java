package com.lijian.game.forum.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
@Data
public class UserVo {
    /**
     * 主键
     */

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
