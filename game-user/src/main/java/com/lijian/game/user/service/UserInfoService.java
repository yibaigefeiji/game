package com.lijian.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.user.entity.UserInfoEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户统计信息
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Service
public interface UserInfoService extends IService<UserInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

