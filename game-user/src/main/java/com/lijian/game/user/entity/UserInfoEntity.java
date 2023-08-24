package com.lijian.game.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户统计信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Data
@TableName("t_user_info")
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 会员id
     */
    private Long userId;
    /**
     * 订单数量
     */
    private Integer orderCount;
    /**
     * 优惠券数量
     */
    private Integer couponCount;
    /**
     * 评价数
     */
    private Integer commentCount;
    /**
     * 关注数量
     */
    private Integer attendCount;
    /**
     * 粉丝数量
     */
    private Integer fansCount;
    /**
     * 收藏的商品数量
     */
    private Integer collectProductCount;

}
