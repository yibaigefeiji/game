package com.lijian.game.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.lijian.game.order.dao")
@SpringBootApplication
public class GameOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameOrderApplication.class, args);
    }

}
