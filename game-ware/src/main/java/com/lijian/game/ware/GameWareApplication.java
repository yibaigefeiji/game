package com.lijian.game.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
//监听消息
@EnableRabbit
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.lijian.game.ware.dao")
@SpringBootApplication
public class GameWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameWareApplication.class, args);
    }

}
