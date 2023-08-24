package com.lijian.game.user.service.impl;

import com.lijian.game.common.utils.R;
import com.lijian.game.user.utils.RedisIdWorker;
import com.lijian.game.user.dto.UserDTO;
import com.lijian.game.user.entity.CouponHistoryEntity;
import com.lijian.game.user.service.CouponHistoryService;
import com.lijian.game.user.utils.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.user.dao.CouponDao;
import com.lijian.game.user.entity.CouponEntity;
import com.lijian.game.user.service.CouponService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.lijian.game.common.constant.CouponConstantt.USER_ORDER;


@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponDao, CouponEntity> implements CouponService {
    @Resource
    CouponService couponService;
    @Resource
    CouponHistoryService couponHistoryService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CouponEntity> page = this.page(
                new Query<CouponEntity>().getPage(params),
                new QueryWrapper<CouponEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R seckillCoupon(Long id) {
        //1.查询优惠券具体信息
        CouponEntity couponEntity = this.baseMapper.selectById(id);
        //2.判断秒杀是否开始
        if (couponEntity.getStartTime().after(new Date())) {
            return R.error("秒杀尚未开始");
        }
        ;
        //3.判断秒杀是否结束
        if (couponEntity.getEndTime().before(new Date())) {
            return R.error("秒杀已经结束");
        }
        //4.判断库存是否充足
        if (couponEntity.getNum() < 1) {
            return R.error("库存不足！");
        }


        return createVoucherOrder(id);
    }

    @Transactional
    public R createVoucherOrder(Long id) {
        //1.一人一单
        UserDTO userDTO = UserHolder.getUser();
        Long userId = userDTO.getId();
        //1.1创建锁对象
        RLock redislock = redissonClient.getLock(USER_ORDER + userId);
        //1.2尝试获取锁
        boolean isLock = redislock.tryLock();
        //1.3判断
        if (!isLock) {
            return R.error("不允许重复下单");
        }
        try {
            //1.4查询订单
            int count = couponHistoryService.query().eq("member_id", userId).eq("coupon_id", id).count();
            //1.5查询是否已经抢购
            if (count > 0) {
                return R.error("您已经抢购过一次了");
            }
            //2.扣减库存
            Boolean success = couponService.update()
                    .setSql("num = num - 1")
                    .eq("id", id)
                    .gt("num", 0)
                    .update();
            if (!success) {
                //扣减失败
                return R.error("库存不足");
            }
            //3.创建订单
            CouponHistoryEntity couponHistoryEntity = new CouponHistoryEntity();
            //3.1订单id
            long orderId = redisIdWorker.nextId("order");
            couponHistoryEntity.setId(orderId);
            //3.2用户id
            couponHistoryEntity.setMemberId(userId);
            //3.3优惠券id
            couponHistoryEntity.setCouponId(id);
            //3.4时间
            couponHistoryEntity.setCreateTime(new Date());
            couponHistoryService.save(couponHistoryEntity);
            //4.返回订单id
            return R.ok().put("data", orderId);

        } finally {
            // 释放锁
            redislock.unlock();
        }


    }

}
