package com.lijian.game.order.feign;


import com.lijian.game.order.DTO.OrderItemDto;
import com.lijian.game.order.config.FeignConfig;
import com.lijian.game.order.vo.CartItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;



/**
 * @author lenovo
 */
@FeignClient("game-cart")
public interface CartFeignService {

    /**
     * 查询当前用户购物车选中的商品项
     * @return
     */
    @GetMapping(value = "cart/currentUserCartItem")
     List<CartItemVo> getCurrentCartItem();

}
