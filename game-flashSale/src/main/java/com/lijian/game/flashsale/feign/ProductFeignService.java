package com.lijian.game.flashsale.feign;


import com.lijian.game.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@FeignClient("game-product")
public interface ProductFeignService {

    @RequestMapping("/product/sku/info/{id}")
    R getSkuInfo(@PathVariable("id") Long id);

}
