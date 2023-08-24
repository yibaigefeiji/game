package com.lijian.game.forum.config;


import com.lijian.game.forum.utils.LoginInterceptor;
import com.lijian.game.forum.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
                    "/forum/forum/**",
                        "/image/**"



                ).order(1);
        // token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }

    /**
     * @desc 静态资源管理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//通过image访问本地的图片
        registry.addResourceHandler("/image/**").addResourceLocations("file:E:/picture/");

    }
}
