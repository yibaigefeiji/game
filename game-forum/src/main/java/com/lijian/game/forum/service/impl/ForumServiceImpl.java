package com.lijian.game.forum.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.lijian.game.common.utils.R;
import com.lijian.game.forum.DTO.UserDTO;
import com.lijian.game.forum.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.forum.dao.ForumDao;
import com.lijian.game.forum.entity.ForumEntity;
import com.lijian.game.forum.service.ForumService;


@Service("forumService")
public class ForumServiceImpl extends ServiceImpl<ForumDao, ForumEntity> implements ForumService {

    @Autowired
    private ForumService forumService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ForumEntity> page = this.page(
                new Query<ForumEntity>().getPage(params),
                new QueryWrapper<ForumEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R postArticle(Map<String, Object> params) {
        ForumEntity forum = new ForumEntity();
        forum.setTitle(String.valueOf(params.get("title")));
        forum.setText(params.get("text").toString());
        forum.setImages(params.get("images").toString());
        forum.setLable(params.get("lable").toString());

        forum.setGameSpu(Long.parseLong(params.get("game_spu").toString()));

        forum.setUserInfo(Long.parseLong(params.get("game_id").toString()));
        forum.setComment(0);
        forum.setLikes(0);
        forum.setTime(new Date());
        System.out.println(forum);
         this.baseMapper.insert(forum);
         Long userId = forum.getUserInfo();
         String key = "feed:" + userId;
         stringRedisTemplate.opsForZSet().add(key,forum.getId().toString(), System.currentTimeMillis());
         
        return R.ok();
    }

    @Override
    public int likes(Long id) {
        UserDTO userDTO = UserHolder.getUser();
        Long userId = userDTO.getId();
        String key = "blog:like:" + id;
        Boolean isLike =  stringRedisTemplate.opsForSet().isMember(key,userId.toString());
        if(BooleanUtil.isFalse(isLike)){
            boolean isSuccess = update().setSql("likes = likes + 1").eq("id",id).update();
            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key,userId.toString());
            }
            return 1;
        }else{
                boolean isSuccess = update().setSql("likes = likes -1").eq("id",id).update();
                stringRedisTemplate.opsForSet().remove(key,userId.toString());
                return  0;
        }

    }

    @Override
    public List<ForumEntity> ArticleById(Long id) {
        List<ForumEntity> forumEntityList = this.baseMapper.selectList(new QueryWrapper<ForumEntity>().eq("user_info",id));
        return forumEntityList;
    }


}
