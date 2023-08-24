package com.lijian.game.user.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lijian.game.user.entity.LikeSpuEntity;
import com.lijian.game.user.service.LikeSpuService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;


/**
 * 用户收藏的商品
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@RestController
@RequestMapping("user/likespu")
public class LikeSpuController {
    @Autowired
    private LikeSpuService likeSpuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("user:likespu:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = likeSpuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("user:likespu:info")
    public R info(@PathVariable("id") Long id) {
        LikeSpuEntity likeSpu = likeSpuService.getById(id);

        return R.ok().put("likeSpu", likeSpu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:likespu:save")
    public R save(@RequestBody LikeSpuEntity likeSpu) {
        likeSpuService.save(likeSpu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //  @RequiresPermissions("user:likespu:update")
    public R update(@RequestBody LikeSpuEntity likeSpu) {
        likeSpuService.updateById(likeSpu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:likespu:delete")
    public R delete(@RequestBody Long[] ids) {
        likeSpuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
