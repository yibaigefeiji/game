package com.lijian.game.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.user.entity.CouponEntity;
import com.lijian.game.user.service.CouponService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;


/**
 * 优惠券信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@RestController
@RequestMapping("user/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 查询优惠券信息
     */
    @GetMapping("/coupon")
    public R coupon() {
        List<CouponEntity> couponEntityList = couponService.list();

        return R.ok().put("data", couponEntityList);
    }

    /**
     * 优惠券秒杀
     */
    @GetMapping("/seckillCoupon")
    public R seckillCoupon(@RequestParam("id") Long id) {
        return couponService.seckillCoupon(id);

    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("user:coupon:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("user:coupon:info")
    public R info(@PathVariable("id") Long id) {
        CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:coupon:save")
    public R save(@RequestBody CouponEntity coupon) {
        couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //  @RequiresPermissions("user:coupon:update")
    public R update(@RequestBody CouponEntity coupon) {
        couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:coupon:delete")
    public R delete(@RequestBody Long[] ids) {
        couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
