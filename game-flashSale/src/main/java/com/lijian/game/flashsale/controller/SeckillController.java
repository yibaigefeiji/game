package com.lijian.game.flashsale.controller;

import com.lijian.game.common.utils.R;
import com.lijian.game.flashsale.service.SeckillService;
import com.lijian.game.flashsale.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor;
import java.util.List;

/**
 * @author lenovo
 */
@RestController
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    /**
     * 当前时间可以参与秒杀的商品信息
     * @return
     */
    @GetMapping(value = "/getCurrentSeckillProducts")
    @ResponseBody
    public R getCurrentSeckillProducts(){
        //获取当前可以秒杀的商品消息
        List<SeckillSkuRedisTo> seckillSkuRedisTos = seckillService.getCurrentSeckillProducts();
        return R.ok().put("data",seckillSkuRedisTos);
    }

    /**
     * 秒杀商品
     */
    @GetMapping(value = "/kill")
    public R seckill(@RequestParam("id") String id,
                     @RequestParam("randomCode") String randomCode) throws InterruptedException {

        String msg = seckillService.kill(id,randomCode);

        return R.ok().put("data",msg);
    }


}
