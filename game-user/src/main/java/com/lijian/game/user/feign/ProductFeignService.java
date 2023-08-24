package com.lijian.game.user.feign;

import com.lijian.game.common.utils.R;
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
     * 根据spuId查询sku信息(供远程调用)
     * @param spuId
     * @return
     */
    @RequestMapping("product/sku/{spuId}/detail")
    R getDetail(@PathVariable("spuId") Long spuId);
}
