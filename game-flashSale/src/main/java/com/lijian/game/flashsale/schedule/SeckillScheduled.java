package com.lijian.game.flashsale.schedule;


import com.lijian.game.flashsale.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


/**
 * 秒杀商品定时上架
 *  每天晚上3点，上架最近三天需要三天秒杀的商品
 *  当天00:00:00 - 23:59:59
 *  明天00:00:00 - 23:59:59
 *  后天00:00:00 - 23:59:59
 */

@Slf4j
@Service
public class SeckillScheduled {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 秒杀商品上架功能的锁
     */
    private final String upload_lock = "seckill:upload:lock";

    @Scheduled(cron = "0 * * * * ?")
    public  void uploadSeckill3Days(){
        log.info("上架秒杀商品");
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10,TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSku3Days();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }


    }


}
