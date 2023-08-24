package com.lijian.game.product.controller;

import java.util.Arrays;
import java.util.Map;


import com.lijian.game.common.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lijian.game.product.entity.SpuDetailEntity;
import com.lijian.game.product.service.SpuDetailService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;



/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@RestController
@RequestMapping("product/spudetail")
public class SpuDetailController {
    @Autowired
    private SpuDetailService spuDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:spudetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spuId}")
   // @RequiresPermissions("product:spudetail:info")
    public R info(@PathVariable("spuId") Long spuId){
		SpuDetailEntity spuDetail = spuDetailService.getById(spuId);

        return R.ok().put("spuDetail", spuDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spudetail:save")
    public R save(@RequestBody SpuDetailEntity spuDetail){
		spuDetailService.save(spuDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("product:spudetail:update")
    public R update(@RequestBody SpuDetailEntity spuDetail){
		spuDetailService.updateById(spuDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spudetail:delete")
    public R delete(@RequestBody Long[] spuIds){
		spuDetailService.removeByIds(Arrays.asList(spuIds));

        return R.ok();
    }

}
