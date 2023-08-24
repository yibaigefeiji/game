package com.lijian.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;
import com.lijian.game.user.entity.CouponEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Service
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 优惠券秒杀
     */
    R seckillCoupon(Long id);
}

