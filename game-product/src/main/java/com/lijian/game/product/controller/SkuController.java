package com.lijian.game.product.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijian.game.product.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.product.entity.SkuEntity;
import com.lijian.game.product.service.SkuService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;



/**
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@RestController
@RequestMapping("product/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;

    /**
     * 根据spuId查询sku信息(供远程调用)
     * @param spuId
     * @return
     */
    @RequestMapping("/{spuId}/detail")
    public R getDetail(@PathVariable("spuId") Long spuId) {

        List<SkuEntity> skuEntity = skuService.list(new QueryWrapper<SkuEntity>().eq("spu_id",spuId));



        return R.ok().setData(skuEntity);
    }

    /**
     * 根据skuId查询当前商品的价格(供远程调用)
     * @param skuId
     * @return
     */
    @RequestMapping("/{skuId}/price")
    public BigDecimal getPrice(@PathVariable("skuId") Long skuId) {

        //获取当前商品的信息
        SkuEntity skuInfo = skuService.getById(skuId);

        //获取商品的价格
        BigDecimal price = BigDecimal.valueOf(skuInfo.getPrice());

        return price;
    }
    /**
     * 游戏详情页
     */
    @RequestMapping("/gameDetail")
    // @RequiresPermissions("product:sku:info")
    public R gameDetail(@RequestParam Long id) throws ExecutionException, InterruptedException {
        List<GameDetialVo> gameDetialVo = skuService.gameDetailById(id);
        System.out.println(gameDetialVo);
        return R.ok().put("data", gameDetialVo);
    }

    /**
     *猜你喜欢
     */
    @RequestMapping("/like")
    // @RequiresPermissions("product:sku:list")
    public R like(){
        List<SkuEntity> skuEntities = skuService.like();
        return R.ok().put("data", skuEntities);

    }

    /**
     * 按游戏标签分类查找游戏形成分类列表
     */
    @RequestMapping("/label")
    // @RequiresPermissions("product:spu:list")
//    @RequestParam Map<String, Object> params
    public R label(){
        List<ArrayList<List<ListVo>>> ListVo = skuService.label();

        return R.ok().put("data", ListVo);
    }



    /**
     * 游戏查询级按价格和销量升降序
     * //TODO 按销量排序
     */
    @RequestMapping("/search")
    // @RequiresPermissions("product:sku:list")
    public R search(@RequestParam Map<String, Object> params){
        List<SkuEntity> skuEntities = skuService.search(params);
        return R.ok().put("data", skuEntities);

    }
    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:sku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("product:sku:info")
    public R info(@PathVariable("id") Long id){
		SkuEntity sku = skuService.getById(id);

        return R.ok().put("sku", sku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:sku:save")
    public R save(@RequestBody SkuEntity sku){
		skuService.save(sku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("product:sku:update")
    public R update(@RequestBody SkuEntity sku){
		skuService.updateById(sku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:sku:delete")
    public R delete(@RequestBody Long[] ids){
		skuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
