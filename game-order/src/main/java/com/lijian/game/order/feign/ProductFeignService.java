package com.lijian.game.order.feign;

import com.lijian.game.common.utils.R;
import com.lijian.game.common.vo.KeyVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

/**
 * @author lenovo
 */
@FeignClient("game-product")
public interface ProductFeignService {




    /**
     * 信息
     */
    @RequestMapping("product/sku/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 根据skuId查询当前商品的最新价格
     *
     * @param skuId
     * @return
     */
    @RequestMapping(value = "product/sku/{skuId}/price")
    BigDecimal getPrice(@PathVariable("skuId") Long skuId);

    /**
     * 根据skuId查询spu的信息
     * @param skuId
     * @return
     */
    @RequestMapping("/product/spu/skuId/{skuId}")
    R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);


    /**
     * 根据skuId查询key的信息
     * @param skuId
     * @return
     */
    @RequestMapping("/product/key/skuId/{skuId}")
    KeyVo getKeyInfoBySkuId(@PathVariable("skuId") Long skuId);
}
