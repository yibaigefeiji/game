package com.lijian.game.user.dto;

import lombok.Data;

import java.util.List;

/*
 * DTO代表服务层需要接收的数据和返回的数据，而VO代表展示层需要显示的数据。
 * */
@Data
public class ScrollResult {
    private List<?> list;
    private Long minTime;
    private Integer offset;
}
