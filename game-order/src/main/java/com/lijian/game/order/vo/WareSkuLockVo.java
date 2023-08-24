package com.lijian.game.order.vo;

import com.lijian.game.order.DTO.OrderItemDto;
import lombok.Data;

import java.util.List;



@Data
public class WareSkuLockVo {

    private String orderSn;

    /** 需要锁住的所有库存信息 **/
    private List<OrderItemDto> locks;



}
