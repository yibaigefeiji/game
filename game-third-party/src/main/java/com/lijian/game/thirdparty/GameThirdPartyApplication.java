package com.lijian.game.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GameThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameThirdPartyApplication.class, args);
    }

}
