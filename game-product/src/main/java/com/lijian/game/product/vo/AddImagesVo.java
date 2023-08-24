package com.lijian.game.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lenovo
 */
@Data
public class AddImagesVo {

    private Long spuId;
    private String imgName;
    private List<String> imgUrl;

}
