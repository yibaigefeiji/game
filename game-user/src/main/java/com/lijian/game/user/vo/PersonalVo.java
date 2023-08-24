package com.lijian.game.user.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PersonalVo {
    private Long id;

    /**
     * 昵称，默认是用户id
     */
    private String nickName;
    /**
     * 人物头像
     */
    private String icon;


    private List<PersonalArticleVo> personalArticleVoList;
}
