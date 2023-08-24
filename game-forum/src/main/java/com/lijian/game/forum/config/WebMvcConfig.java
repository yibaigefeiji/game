package com.lijian.game.forum.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * @desc 静态资源管理
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//通过image访问本地的图片
		registry.addResourceHandler("/image/**").addResourceLocations("file:E:/picture/");

	}


}
