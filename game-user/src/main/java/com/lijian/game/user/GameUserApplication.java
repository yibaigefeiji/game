package com.lijian.game.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author lenovo
 */
@EnableFeignClients
@EnableRedisHttpSession     //开启springsession
@EnableDiscoveryClient
@MapperScan("com.lijian.game.user.dao")
@SpringBootApplication
public class GameUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameUserApplication.class, args);
    }

}
