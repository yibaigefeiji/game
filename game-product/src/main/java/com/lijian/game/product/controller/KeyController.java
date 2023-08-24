package com.lijian.game.product.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lijian.game.product.entity.KeyEntity;
import com.lijian.game.product.service.KeyService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;



/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-25 10:52:11
 */
@RestController
@RequestMapping("product/key")
public class KeyController {
    @Autowired
    private KeyService keyService;

    /**
     * 根据skuId查询key的信息
     * @param skuId
     * @return
     */
    @RequestMapping("/skuId/{skuId}")
    public KeyEntity getKeyInfoBySkuId(@PathVariable("skuId") Long skuId){
       KeyEntity key = keyService.getBySku(skuId);
       return key;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:key:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = keyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("product:key:info")
    public R info(@PathVariable("id") Long id){
		KeyEntity key = keyService.getById(id);

        return R.ok().put("key", key);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:key:save")
    public R save(@RequestBody KeyEntity key){
		keyService.save(key);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("product:key:update")
    public R update(@RequestBody KeyEntity key){
		keyService.updateById(key);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:key:delete")
    public R delete(@RequestBody Long[] ids){
		keyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
