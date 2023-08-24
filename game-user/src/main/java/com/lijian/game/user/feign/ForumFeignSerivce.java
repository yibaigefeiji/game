package com.lijian.game.user.feign;

import com.lijian.game.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("game-forum")
public interface ForumFeignSerivce {

    @RequestMapping("forum/forum/ArticleList")
    R ArticleList();

    /**
     * 根据用户id查询所发布的文章
     */
    @RequestMapping("forum/forum/ArticleById")
    R ArticleById(@RequestParam("id") Long id);
}
