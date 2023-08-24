package com.lijian.game.flashsale.feign;

import com.lijian.game.common.utils.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;



/**
 * @author lenovo
 */
@FeignClient("game-user")
public interface SeckillFeignService {

    /**
     * 查询最近三天需要参加秒杀商品的信息
     * @return
     */
    @GetMapping(value = "/user/seckill/3DayProduct")
    R get3DayProduct();

}
