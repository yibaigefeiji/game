package com.lijian.game.user.service.impl;

import com.lijian.game.user.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.user.dao.FollowDao;
import com.lijian.game.user.entity.FollowEntity;
import com.lijian.game.user.service.FollowService;


@Service("followService")
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FollowEntity> page = this.page(
                new Query<FollowEntity>().getPage(params),
                new QueryWrapper<FollowEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String follow(Long id, Boolean follow) {
        Long userId = UserHolder.getUser().getId();
        String key = "follows:"+userId;
        if(follow){

            FollowEntity followEntity = new FollowEntity();
            followEntity.setFollowId(id);
            followEntity.setUserId(userId);
            followEntity.setCreatTime(new Date());
            Boolean isSuccess = save(followEntity);

            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key,id.toString());
            }
            return "yes";
        }else{
           Boolean isSuccess =  remove(new QueryWrapper<FollowEntity>()
                    .eq("user_id",userId).eq("follow_id",id));

           if(isSuccess){
               stringRedisTemplate.opsForSet().remove(key,id);
           }
            return "no";
        }

    }

    @Override
    public String isFollow(Long id) {
        Long userId = UserHolder.getUser().getId();
        Integer count = query().eq("user_id", userId).eq("follow_id", id).count();
        if(count > 0){
            return "yes";
        }else {
            return "no";
        }

    }


}
