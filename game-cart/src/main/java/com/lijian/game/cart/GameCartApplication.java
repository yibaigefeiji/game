package com.lijian.game.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession     //开启springsession
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class GameCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameCartApplication.class, args);
    }

}
