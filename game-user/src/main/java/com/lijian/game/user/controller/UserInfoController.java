package com.lijian.game.user.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lijian.game.user.entity.UserInfoEntity;
import com.lijian.game.user.service.UserInfoService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;


/**
 * 用户统计信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@RestController
@RequestMapping("user/userinfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("user:userinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("user:userinfo:info")
    public R info(@PathVariable("id") Long id) {
        UserInfoEntity userInfo = userInfoService.getById(id);

        return R.ok().put("userInfo", userInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:userinfo:save")
    public R save(@RequestBody UserInfoEntity userInfo) {
        userInfoService.save(userInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //  @RequiresPermissions("user:userinfo:update")
    public R update(@RequestBody UserInfoEntity userInfo) {
        userInfoService.updateById(userInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:userinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        userInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
