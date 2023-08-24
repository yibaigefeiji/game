package com.lijian.game.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@EnableFeignClients(basePackages = "com.lijian.game.product.feign")
@EnableRedisHttpSession     //开启springsession
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.lijian.game.product.dao")
@SpringBootApplication
public class GameProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameProductApplication.class, args);
    }

}
