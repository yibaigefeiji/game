package com.lijian.game.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelVo {
    private String name;

    private List<Data> data;


}
