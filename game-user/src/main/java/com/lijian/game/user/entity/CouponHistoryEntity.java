package com.lijian.game.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 优惠券领取历史记录
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Data
@TableName("t_coupon_history")
public class CouponHistoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 会员名字
     */
    private String memberNickName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 使用状态[0->未使用；1->已使用；2->已过期]
     */
    private Integer useType;
    /**
     * 使用时间
     */
    private Date useTime;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 订单号
     */
    private Long orderSn;

}
