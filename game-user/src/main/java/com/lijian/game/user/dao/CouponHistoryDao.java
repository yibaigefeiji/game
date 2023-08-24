package com.lijian.game.user.dao;

import com.lijian.game.user.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {

}
