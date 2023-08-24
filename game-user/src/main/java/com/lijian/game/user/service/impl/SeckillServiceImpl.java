package com.lijian.game.user.service.impl;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.user.dao.SeckillDao;
import com.lijian.game.user.entity.SeckillEntity;
import com.lijian.game.user.service.SeckillService;


@Service("seckillService")
public class SeckillServiceImpl extends ServiceImpl<SeckillDao, SeckillEntity> implements SeckillService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillEntity> page = this.page(
                new Query<SeckillEntity>().getPage(params),
                new QueryWrapper<SeckillEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillEntity> getLates3DaySession() {
        //计算最近三天
        //查出这三天参与秒杀活动的商品
        return this.baseMapper.
                selectList(new QueryWrapper<SeckillEntity>()
                        .between("start_time",startTime(),endTime()));
    }

    /**
     * 当前时间
     * @return
     */
    private String startTime() {
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime start = LocalDateTime.of(now, min);


        //格式化时间
        String startFormat = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return startFormat;
    }

    /**
     * 结束时间
     * @return
     */
    private String endTime() {
        LocalDate now = LocalDate.now();
        LocalDate plus = now.plusDays(2);
        LocalTime max = LocalTime.MAX;
        LocalDateTime end = LocalDateTime.of(plus, max);

        //格式化时间
        String endFormat = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return endFormat;
    }
}
