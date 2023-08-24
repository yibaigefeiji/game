package com.lijian.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;
import com.lijian.game.user.dto.LoginFormDTO;
import com.lijian.game.user.dto.RegisterFormDTO;
import com.lijian.game.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Service
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 发送手机验证码
     */
    R sendCode(String phone, HttpSession session);

    /**
     * 登录
     */
    R login(LoginFormDTO loginForm, HttpSession session);

    /**
     * 注册功能
     */
    R register(RegisterFormDTO registerForm, HttpSession session);

    void news();

    List<UserEntity> allFollows(Long id);

    List<UserEntity> myFollows(Long id);

    List<UserEntity> myFans(Long id);
}

