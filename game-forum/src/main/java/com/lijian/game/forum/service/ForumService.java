package com.lijian.game.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;
import com.lijian.game.forum.entity.ForumEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2023-02-23 21:16:47
 */
@Service
public interface ForumService extends IService<ForumEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R postArticle(Map<String, Object> params);

    int likes(Long id);


    List<ForumEntity> ArticleById(Long id);
}

