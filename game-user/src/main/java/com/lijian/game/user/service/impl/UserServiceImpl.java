package com.lijian.game.user.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.lijian.game.common.utils.R;

import com.lijian.game.user.dao.FollowDao;
import com.lijian.game.user.dto.LoginFormDTO;
import com.lijian.game.user.dto.RegisterFormDTO;
import com.lijian.game.user.dto.UserDTO;
import com.lijian.game.user.entity.FollowEntity;
import com.lijian.game.user.utils.RedisConstants;
import com.lijian.game.user.utils.SmsComponent;
import com.lijian.game.user.utils.UserHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.user.dao.UserDao;
import com.lijian.game.user.entity.UserEntity;
import com.lijian.game.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * @author lenovo
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SmsComponent smsComponent;
    @Resource
    private FollowDao followDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R sendCode(String phone, HttpSession session) {
        //1.验证手机号是否合法
//        if (RegexUtils.isPhoneInvalid(phone)) {
//            // 2.如果不符合，返回错误信息
//            return R.error("手机号格式错误！");
//        }
        //验证成功，随机生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 4.保存验证码到 redis
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 5.发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
        smsComponent.sendSmsCode(phone, code);

        return R.ok();
    }

    @Override
    public R login(LoginFormDTO loginForm, HttpSession session) {
        //判断是哪种登录类型(验证码，密码)
        if (loginForm.getCode() != null) {
            // 1.校验手机号
            String phone = loginForm.getPhone();
//            if (RegexUtils.isPhoneInvalid(phone)) {
//                // 2.如果不符合，返回错误信息
//                return R.error("手机号格式错误！");
//            }
            // 3.从redis获取验证码并校验
            String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
            String code = loginForm.getCode();
            if (cacheCode == null || !cacheCode.toString().equals(code)) {
                return R.error("验证码错误");
            }
            // 4.一致，根据手机号查询用户 select * from t_user where phone = ?
            UserEntity userEntity = query().eq("phone", phone).one();
            //判断用户是否存在
            if (userEntity == null) {
                // 6.不存在，创建新用户并保存
                userEntity = createUserWithPhone(phone);
            }
            // 7.保存用户信息到 redis中
            // 7.1.随机生成token，作为登录令牌
            String token = UUID.randomUUID().toString();
            //7.2将user对象转为Hashmap存储
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
            System.out.println(userDTO);
            Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
            // 7.3.存储
            String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
            stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
            // 7.4.设置token有效期
            stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
            // 8.返回token
            return R.ok().put("data", token).put("code", 200).put("userdata", userDTO);
        } else {
            // 1.校验手机号
            String phone = loginForm.getPhone();
//            if (RegexUtils.isPhoneInvalid(phone)) {
//                // 2.如果不符合，返回错误信息
//                return R.error("手机号格式错误！");
//            }
            // 2，根据手机号查询用户 select * from t_user where phone = ?
            UserEntity userEntity = query().eq("phone", phone).one();
            //判断用户是否存在
            if (userEntity == null) {
                //不存在，返回错误信息
                return R.error("该用户不存在");
            }
            if (Objects.equals(userEntity.getPassword(), loginForm.getPassword())) {
                // 保存用户信息到 redis中
                // 随机生成token，作为登录令牌
                String token = UUID.randomUUID().toString();
                //将user对象转为Hashmap存储
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(userEntity, userDTO);
                System.out.println(userDTO);
                Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                        CopyOptions.create()
                                .setIgnoreNullValue(true)
                                .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
                // 存储
                String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
                stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
                // .设置token有效期
//                stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
                // 返回token
                return R.ok().put("data", token).put("code", 200).put("userdata", userDTO);
            } else {
                return R.error("用户名或密码错误");
            }


        }

    }

    @Override
    public R register(RegisterFormDTO registerForm, HttpSession session) {
        // 1.校验手机号
        String phone = registerForm.getPhone();
//        if (RegexUtils.isPhoneInvalid(phone)) {
//            // 2.如果不符合，返回错误信息
//            return R.error("手机号格式错误！");
//        }
        // 3.从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        String code = registerForm.getCode();
        if (cacheCode == null || !cacheCode.toString().equals(code)) {
            return R.error("验证码错误");
        }
        // 4.一致，根据手机号查询用户 select * from t_user where phone = ?
        UserEntity userEntity = query().eq("phone", phone).one();
        //判断用户是否存在
        if (userEntity == null) {
            // 6.不存在，创建新用户并保存
            UserEntity userEntity1 = new UserEntity();
            userEntity1.setNickName("user_" + RandomUtil.randomString(10));
            userEntity1.setPhone(registerForm.getPhone());
            userEntity1.setPassword(registerForm.getPassword());
            save(userEntity1);
            // 7.保存用户信息到 redis中
            // 7.1.随机生成token，作为登录令牌
            String token = UUID.randomUUID().toString();
            //7.2将user对象转为Hashmap存储
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity1, userDTO);
            Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
            // 7.3.存储
            String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
            stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
            // 7.4.设置token有效期
            stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
            // 8.返回token
            return R.ok().put("data", token).put("code", 200).put("userdata", userDTO);
        } else {
            return R.error("该账户已存在");
        }


    }

    @Override
    public void news() {

        for (int i = 0; i < 100; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setNickName("user_" + RandomUtil.randomString(10));
            userEntity.setPhone(RandomUtil.randomNumbers(11));
            userEntity.setPassword("123456");
            save(userEntity);
        }

    }

    @Override
    public List<UserEntity> allFollows(Long id) {
        String key1 = "follows:" + UserHolder.getUser().getId();
        String key2 = "follows:" + id;
        Set<String>  stringSet = stringRedisTemplate.opsForSet().intersect(key1,key2);
        List<Long> collect = stringSet.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserEntity> userEntities = this.baseMapper.selectBatchIds(collect);
        return userEntities;
    }

    @Override
    public List<UserEntity> myFollows(Long id) {
        String key = "follows:" + id;
        Set<String> ids = stringRedisTemplate.opsForSet().members(key);
        List<Long> collect = ids.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserEntity> userEntityList = this.baseMapper.selectBatchIds(collect);
        return userEntityList;
    }

    @Override
    public List<UserEntity> myFans(Long id) {
        List<FollowEntity> followEntityList = followDao.
                selectList(new QueryWrapper<FollowEntity>().eq("follow_id",id));
        List<Long> ids = new ArrayList<>();
        followEntityList.stream().filter(item -> item.getFollowId().equals(id))
                .map(item ->{
                   ids.add(item.getUserId());
                   return null;
                }).collect(Collectors.toList());

        List<UserEntity> userEntityList = this.baseMapper.selectBatchIds(ids);
        return userEntityList;
    }

    private UserEntity createUserWithPhone(String phone) {

        UserEntity userEntity = new UserEntity();
        userEntity.setPhone(phone);
        userEntity.setNickName("user_" + RandomUtil.randomString(10));
        save(userEntity);
        return userEntity;
    }

}
