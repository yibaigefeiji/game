package com.lijian.game.forum.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {
    /**
     * 所有对Redisson的使用都是通过RedissonClient
     *
     * @return
     * @throws IOException
     * @Bean(destroyMethod = "shutdown")嵌入式数据库：适合于开发环境
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        //1、创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.101.4:6379").setPassword("123456");

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
