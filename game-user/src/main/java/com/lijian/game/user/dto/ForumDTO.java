package com.lijian.game.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ForumDTO {
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
    private String comment;
    /**
     *
     */
    private String likes;
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
