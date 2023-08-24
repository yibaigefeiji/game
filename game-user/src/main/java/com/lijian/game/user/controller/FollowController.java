package com.lijian.game.user.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lijian.game.user.entity.FollowEntity;
import com.lijian.game.user.service.FollowService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;



/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2023-02-27 13:35:28
 */
@RestController
@RequestMapping("user/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    /**
     * 关注功能
     */
    @RequestMapping("follow")
    public R follow(@RequestParam("id") Long id ,@RequestParam("follow") Boolean follow){
        System.out.println(id);
        System.out.println(follow);
        String s = followService.follow(id,follow);
        return R.ok().setData(s);
    }

    /**
     * 查询是否关注
     */
    @RequestMapping("isFollow")
    public R isFollow(@RequestParam("id") Long id){
        System.out.println(id);
        String s = followService.isFollow(id);
        return R.ok().setData(s);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("user:follow:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = followService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("user:follow:info")
    public R info(@PathVariable("id") Long id){
		FollowEntity follow = followService.getById(id);

        return R.ok().put("follow", follow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:follow:save")
    public R save(@RequestBody FollowEntity follow){
		followService.save(follow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("user:follow:update")
    public R update(@RequestBody FollowEntity follow){
		followService.updateById(follow);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:follow:delete")
    public R delete(@RequestBody Long[] ids){
		followService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
