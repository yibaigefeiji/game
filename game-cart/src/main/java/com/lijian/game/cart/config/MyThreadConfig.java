package com.lijian.game.cart.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池配置类
 * @Created: with IntelliJ IDEA.
 * @author: lj
 **/

@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@Configuration
public class MyThreadConfig {
/*
* int corePoolSize,
常驻线程数量（核心)
int maximumPoolSize,
最大线程数量
long keepAliveTime,
线程存活时间
TimeUnit unit,
BlockingQueue<Runnable> workQueue,
阻塞队列
ThreadFactory threadFactory,
线程工厂
RejectedExecutionHandler handler
拒绝策略

JDK内置的拒绝策略

AbortPolicy(默认∶直接抛出RejectedExecutionException异常阻止系统正常运行
callerRunsPolicy :“调用者运行“—种调节机制，该策略既不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者，从而降低新任务的流量。
* */

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(
                pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

}
