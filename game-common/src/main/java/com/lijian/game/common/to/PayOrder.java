package com.lijian.game.common.to;



import com.lijian.game.common.vo.OrderDetailVo;
import lombok.Data;

import java.util.List;


/**
 * @author lenovo
 */
@Data
public class PayOrder {

    private String orderSn;

    private List<OrderDetailVo> orderItems;





}
