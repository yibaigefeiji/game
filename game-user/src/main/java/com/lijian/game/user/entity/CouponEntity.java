package com.lijian.game.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 优惠券信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Data
@TableName("t_coupon")
public class CouponEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 优惠券图片
     */
    private String couponImg;
    /**
     * 优惠卷名字
     */
    private String couponName;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 每人限领张数
     */
    private Integer perLimit;
    /**
     * 使用门槛
     */
    private BigDecimal minPoint;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 备注
     */
    private String note;
    /**
     * 发行数量
     */
    private Integer publishCount;
    /**
     * 优惠码
     */
    private String code;
    /**
     * 发布状态[0-未发布，1-已发布]
     */
    private Integer publish;

}
