package com.lijian.game.user.vo;

import lombok.Data;

import java.util.Date;
@Data
public class PersonalArticleVo {
    private Long id;
    /**
     *
     */
    private String title;
    /**
     *
     */
    private String text;
    /**
     *
     */
    private String images;
    /**
     *
     */
    private Long userInfo;
    /**
     *
     */
    private Integer comment;
    /**
     *
     */
    private Integer likes;
    /**
     *
     */
    private Date time;
    /**
     *
     */
    private String lable;
    /**
     *
     */
    private String gameName;
    /**
     *
     */
    private Long gameSpu;
}
