package com.lijian.game.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单
 * 
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 12:54:09
 */
@Data
@TableName("t_order")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * member_id
	 */
	private Long memberId;
	/**
	 * 订单号
	 */
	private String orderSn;
	/**
	 * 使用的优惠券
	 */
	private Long couponId;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * 用户名
	 */
	private String memberUsername;
	/**
	 * 订单总额
	 */
	private BigDecimal totalAmount;
	/**
	 * 应付总额
	 */
	private BigDecimal payAmount;
	/**
	 * 促销优化金额（促销价、满减、阶梯价）
	 */
	private BigDecimal promotionAmount;
	/**
	 * 优惠券抵扣金额
	 */
	private BigDecimal couponAmount;
	/**
	 * 后台调整订单使用的折扣金额
	 */
	private BigDecimal discountAmount;
	/**
	 * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
	 */
	private Integer payType;
	/**
	 * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
	 */
	private Integer status;
	/**
	 * 支付时间
	 */
	private Date paymentTime;
	/**
	 * 评价时间
	 */
	private Date commentTime;
	/**
	 * 修改时间
	 */
	private Date modifyTime;

}
