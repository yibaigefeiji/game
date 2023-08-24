package com.lijian.game.user.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.lijian.game.user.dto.UserDTO;
import com.lijian.game.user.entity.CouponEntity;
import com.lijian.game.user.service.CouponService;
import com.lijian.game.user.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lijian.game.user.entity.CouponHistoryEntity;
import com.lijian.game.user.service.CouponHistoryService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;


/**
 * 优惠券领取历史记录
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@RestController
@RequestMapping("user/couponhistory")
public class CouponHistoryController {
    @Autowired
    private CouponHistoryService couponHistoryService;
    @Autowired
    private CouponService couponService;
    /**
     * 优惠券查询
     */
    @RequestMapping("/coupon")
    // @RequiresPermissions("user:couponhistory:list")
    public R couponList() {
        UserDTO userDTO = UserHolder.getUser();
        Long id = userDTO.getId();
        List<CouponHistoryEntity> couponHistoryEntityList =  couponHistoryService.list();
        List<CouponHistoryEntity> collect = couponHistoryEntityList.stream().filter(lists -> {
            return lists.getMemberId().equals(id);
        }).collect(Collectors.toList());

        System.out.println("sdaas   :"+ collect);
        List<CouponEntity> couponEntityList = new ArrayList<>();
        for (int i = 0; i < couponHistoryEntityList.size(); i++) {
            Long ids =  couponHistoryEntityList.get(i).getCouponId();
            CouponEntity couponEntity = couponService.getById(ids);
            couponEntityList.add(couponEntity);

        }
        return R.ok().put("page", couponEntityList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("user:couponhistory:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("user:couponhistory:info")
    public R info(@PathVariable("id") Long id) {
        CouponHistoryEntity couponHistory = couponHistoryService.getById(id);

        return R.ok().put("couponHistory", couponHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:couponhistory:save")
    public R save(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.save(couponHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //  @RequiresPermissions("user:couponhistory:update")
    public R update(@RequestBody CouponHistoryEntity couponHistory) {
        couponHistoryService.updateById(couponHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:couponhistory:delete")
    public R delete(@RequestBody Long[] ids) {
        couponHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
