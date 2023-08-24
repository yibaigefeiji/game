package com.lijian.game.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.lijian.game.common.exception.NoStockException;
import com.lijian.game.ware.vo.SkuHasStockVo;
import com.lijian.game.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.ware.entity.WareSkuEntity;
import com.lijian.game.ware.service.WareSkuService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;

import static com.lijian.game.common.exception.BizCodeEnume.NO_STOCK_EXCEPTION;


/**
 * 商品库存
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-21 13:18:00
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 锁定库存
     * @param vo
     *
     * 库存解锁的场景
     *      1）、下订单成功，订单过期没有支付被系统自动取消或者被用户手动取消，都要解锁库存
     *      2）、下订单成功，库存锁定成功，接下来的业务调用失败，导致订单回滚。之前锁定的库存就要自动解锁
     *      3）、
     *
     * @return
     */
    @PostMapping(value = "/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo) {

        try {
            boolean lockStock = wareSkuService.orderLockStock(vo);
            System.out.println(145345435);
            return R.ok().setData(lockStock);

        } catch (NoStockException e) {
            System.out.println("sdsadsada");
            return R.error(NO_STOCK_EXCEPTION.getCode(),NO_STOCK_EXCEPTION.getMsg());
        }
    }

    /**
     * 查询sku是否有库存(供远程调用)
     * @return
     */

    @PostMapping(value = "/hasStock")
    public  R getSkuHasStock(@RequestBody List<Long> skuIds){
        //skuId stock
        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStock(skuIds);
        return R.ok().setData(vos);
    };

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
