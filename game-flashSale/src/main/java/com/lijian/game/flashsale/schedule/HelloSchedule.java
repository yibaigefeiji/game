package com.lijian.game.flashsale.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lenovo
 * @Component泛指各种组件，
 * 就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。
 */
@Slf4j
@Component
@EnableScheduling
public class HelloSchedule {
    /**
     * 1、在Spring中表达式是6位组成，不允许第七位的年份
     * 2、在周几的的位置,1-7代表周一到周日
     * 3、定时任务不该阻塞。默认是阻塞的
     *      1）、可以让业务以异步的方式，自己提交到线程池
     *              CompletableFuture.runAsync(() -> {
     *         },execute);
     *
     *      2）、支持定时任务线程池；设置 TaskSchedulingProperties
     *        spring.task.scheduling.pool.size: 5
     *
     *      3）、让定时任务异步执行
     *          异步任务
     *
     *      解决：使用异步任务 + 定时任务来完成定时任务不阻塞的功能
     *
     */
//     @Async
//     @Scheduled(cron = "*/5 * * ? * 4")
//     public void hello() {
//         log.info("hello...");
//         try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
//
//     }
}
