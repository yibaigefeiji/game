package com.lijian.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.user.entity.SeckillEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动商品
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-28 09:29:31
 */
@Service
public interface SeckillService extends IService<SeckillEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 查询最近三天需要参加秒杀商品的信息
     * @return
     */
    List<SeckillEntity> getLates3DaySession();
}

