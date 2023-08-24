package com.lijian.game.user.feign;

import com.lijian.game.common.utils.R;
import com.lijian.game.common.vo.OrderDetailVo;
import com.lijian.game.user.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/**
 * @author lijian
 */
@FeignClient(name = "game-order",configuration = FeignConfig.class)
public interface OrderFeignService {

    @GetMapping(value = "/order/order/status/{orderSn}")
    R getOrderStatus(@PathVariable("orderSn") String orderSn);


    @GetMapping(value = "/order/order/orderItem/{id}")
    List<OrderDetailVo> getOrderItem(@PathVariable("id") Long id);

}
