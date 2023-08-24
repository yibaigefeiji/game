package com.lijian.game.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.lijian.game.product.vo.RecommendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.product.entity.SpuEntity;
import com.lijian.game.product.service.SpuService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;



/**
 * spu表描述的是一个抽象性的商品，比如 iphone8
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:10
 */
@RestController
@RequestMapping("product/spu")
public class SpuController {
    @Autowired
    private SpuService spuService;


    @RequestMapping("/listA")
    // @RequiresPermissions("product:spu:list")
    public R listA(){
        List<SpuEntity> spuEntities = spuService.list();
        return R.ok().setData(spuEntities);
    }



    /**
     * 根据skuId查询spu的信息(供远程调用)
     * @param skuId
     * @return
     */
    @GetMapping(value = "/skuId/{skuId}")
    public R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId) {

        SpuEntity spuEntity = spuService.getSpuInfoBySkuId(skuId);

        return R.ok().setData(spuEntity);
    }


    /**
     * 爆款列表展示
     */
    @RequestMapping("/recommend")
    // @RequiresPermissions("product:spu:list")
//    @RequestParam Map<String, Object> params
    public R recommend(){
        List<RecommendVo> recommendVo = spuService.queryRecommend();

        return R.ok().put("data", recommendVo);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:spu:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("product:spu:info")
    public R info(@PathVariable("id") Long id){
		SpuEntity spu = spuService.getById(id);

        return R.ok().put("spu", spu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spu:save")
    public R save(@RequestBody SpuEntity spu){
		spuService.save(spu);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("product:spu:update")
    public R update(@RequestBody SpuEntity spu){
		spuService.updateById(spu);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spu:delete")
    public R delete(@RequestBody Long[] ids){
		spuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
