package com.lijian.game.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.user.entity.SeckillEntity;
import com.lijian.game.user.service.SeckillService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;



/**
 * 秒杀活动商品
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-28 09:29:31
 */
@RestController
@RequestMapping("user/seckill")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;



    /**
     * 查询最近三天需要参加秒杀商品的信息
     * @return
     */
    @GetMapping(value = "/3DayProduct")
    public R get3DayProduct() {

        List<SeckillEntity> seckillSessionEntities = seckillService.getLates3DaySession();

        return R.ok().setData(seckillSessionEntities);
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("user:seckill:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("user:seckill:info")
    public R info(@PathVariable("id") Long id){
		SeckillEntity seckill = seckillService.getById(id);

        return R.ok().put("seckill", seckill);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:seckill:save")
    public R save(@RequestBody SeckillEntity seckill){
		seckillService.save(seckill);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("user:seckill:update")
    public R update(@RequestBody SeckillEntity seckill){
		seckillService.updateById(seckill);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:seckill:delete")
    public R delete(@RequestBody Long[] ids){
		seckillService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
