package com.lijian.game.user.config;


import com.lijian.game.user.utils.LoginInterceptor;
import com.lijian.game.user.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/user/**",
                        "/user/coupon/list",
                        "/user/coupon/info/{id}",
                        "/user/coupon/save",
                        "/user/coupon/update",
                        "/user/coupon/delete",
                        "/user/couponhistory/**",
                        "/user/coupon/coupon",
                        "/user/coupon/seckillCoupon",
                        "/user/seckill/list",
                        "/user/seckill/info/{id}",
                        "/user/seckill/save",
                        "/user/seckill/update",
                        "/user/seckill/delete",
                        "/user/seckill/3DayProduct"
                ).order(1);
        // token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }
}
