package com.lijian.game.order.config;


import com.lijian.game.order.utils.LoginInterceptor;

import com.lijian.game.order.utils.RefreshTokenInterceptor;
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
                        "/order/order/status/{orderSn}",
                        "/order/order/orderItem/{id}",
                        "/order/order/list",
                        "/order/order/info/{id}",
                        "/order/order/save",
                        "/order/order/update",
                        "/order/order/delete",
                        "/order/order/detail",
                        "/order/orderitem/list",
                        "/order/orderitem/save",
                        "/order/orderitem/update",
                        "/order/orderitem/delete",
                        "/payed/notify"

                ).order(1);
        // token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }
}
