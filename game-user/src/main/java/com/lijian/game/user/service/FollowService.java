package com.lijian.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;

import com.lijian.game.user.entity.FollowEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2023-02-27 13:35:28
 */
@Service
public interface FollowService extends IService<FollowEntity> {

    PageUtils queryPage(Map<String, Object> params);


    String follow(Long id, Boolean follow);

    String isFollow(Long id);
}

