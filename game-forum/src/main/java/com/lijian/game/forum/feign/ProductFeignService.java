package com.lijian.game.forum.feign;

import com.lijian.game.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
/**
 * @author lenovo
 */
@FeignClient("game-product")
public interface ProductFeignService {

    /**
     * 列表
     */
    @RequestMapping("product/spu/listA")
    R listA();
}
