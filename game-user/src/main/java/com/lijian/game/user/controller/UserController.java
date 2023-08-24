package com.lijian.game.user.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lijian.game.common.to.PayOrder;
import com.lijian.game.common.vo.OrderDetailVo;
import com.lijian.game.user.dao.UserDao;
import com.lijian.game.user.dto.ForumDTO;
import com.lijian.game.user.dto.LoginFormDTO;
import com.lijian.game.user.dto.RegisterFormDTO;
import com.lijian.game.user.dto.UserDTO;
import com.lijian.game.user.feign.ForumFeignSerivce;
import com.lijian.game.user.feign.OrderFeignService;
import com.lijian.game.user.feign.ProductFeignService;
import com.lijian.game.user.utils.UserHolder;
import com.lijian.game.user.vo.PersonalArticleVo;
import com.lijian.game.user.vo.PersonalVo;
import com.lijian.game.user.vo.SkuVo;
import com.lijian.game.user.vo.listVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.user.entity.UserEntity;
import com.lijian.game.user.service.UserService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


/**
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-13 16:05:09
 */
@RestController
@RequestMapping("user/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderFeignService orderFeignService;
    @Autowired
    UserDao userDao;
    @Autowired
    private ForumFeignSerivce forumFeignSerivce;
    @Autowired
    private ProductFeignService productFeignService;


    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".")+1);
        } else {
            return "";
        }
    }

    /**
     * 我的关注
     */
    @RequestMapping("myFollows")
    public R myFollows(@RequestParam("id") Long id){
        List<UserEntity> userEntityList = userService.myFollows(id);

        return R.ok().setData(userEntityList);
    }

    /**
     * 我的粉丝
     */
    @RequestMapping("myFans")
    public R myFans(@RequestParam("id") Long id){
        List<UserEntity> userEntityList = userService.myFans(id);

        return R.ok().setData(userEntityList);
    }

    /**
     * 共同关注
     */
    @RequestMapping("allFollows")
    public R allFollows(@RequestParam("id") Long id){
         List<UserEntity> list = userService.allFollows(id);
         return R.ok().setData(list);
    }

    /**
     * 个人详情页面
     */
    @RequestMapping("personal")
    public R Personal(@RequestParam("id") Long id){
        UserEntity userEntity = userService.getById(id);
        List<PersonalArticleVo> personalArticleVoList = forumFeignSerivce.ArticleById(id)
                .getData("data",new TypeReference<List<PersonalArticleVo>>(){});
        PersonalVo personalVo = new PersonalVo();
        personalVo.setId(id);
        personalVo.setIcon(userEntity.getIcon());
        personalVo.setNickName(userEntity.getNickName());
        personalVo.setPersonalArticleVoList(personalArticleVoList);
        return  R.ok().setData(personalVo);
    }


    /**
     * 文章详情
     */
    @RequestMapping("ArticleDetail")
    public R ArticleDetail(@RequestParam Long id)throws ExecutionException, InterruptedException{
        List<ForumDTO> forumDTO = forumFeignSerivce.ArticleList().getData("data",new TypeReference<List<ForumDTO>>(){});
        listVo listVo = new listVo();
        for (int i = 0; i < forumDTO.size(); i++) {
            if(forumDTO.get(i).getId().equals(id)) {

                UserEntity userEntity = userService.getById(forumDTO.get(i).getUserInfo());
                List<SkuVo> skuVo = productFeignService.getDetail(forumDTO.get(i).getGameSpu()).getData("data",new TypeReference<List<SkuVo>>(){});
                listVo.setId(forumDTO.get(i).getId());
                listVo.setComment(forumDTO.get(i).getComment());
                listVo.setGameName(forumDTO.get(i).getGameName());
                listVo.setGameSpu(forumDTO.get(i).getGameSpu());
                listVo.setImages(forumDTO.get(i).getImages());
                listVo.setLikes(forumDTO.get(i).getLikes());
                listVo.setLable(forumDTO.get(i).getLable());
                listVo.setUserInfo(forumDTO.get(i).getUserInfo());
                listVo.setText(forumDTO.get(i).getText());
                listVo.setTitle(forumDTO.get(i).getTitle());
                listVo.setTime(forumDTO.get(i).getTime());
                listVo.setIcon(userEntity.getIcon());
                listVo.setNickName(userEntity.getNickName());
                listVo.setImagesP(skuVo.get(0).getImages());
                listVo.setTitleP(skuVo.get(0).getTitle());
                listVo.setPrice(skuVo.get(0).getPrice());
            }

        }

        return R.ok().setData(listVo);
    }

    /**
     * 文章列表
     */
    @RequestMapping("ArticleList")
    public R ArticleList(){
        List<ForumDTO> forumDTO = forumFeignSerivce.ArticleList().getData("data",new TypeReference<List<ForumDTO>>(){});
        List<listVo> listVos = new ArrayList<>();
        for (int i = 0; i < forumDTO.size(); i++) {
            listVo listVo = new listVo();
            UserEntity userEntity = userService.getById(forumDTO.get(i).getUserInfo());
            listVo.setId(forumDTO.get(i).getId());
            listVo.setComment(forumDTO.get(i).getComment());
            listVo.setGameName(forumDTO.get(i).getGameName());
            listVo.setGameSpu(forumDTO.get(i).getGameSpu());
            listVo.setImages(forumDTO.get(i).getImages());
            listVo.setLikes(forumDTO.get(i).getLikes());
            listVo.setLable(forumDTO.get(i).getLable());
            listVo.setUserInfo(forumDTO.get(i).getUserInfo());
            listVo.setText(forumDTO.get(i).getText());
            listVo.setTitle(forumDTO.get(i).getTitle());
            listVo.setTime(forumDTO.get(i).getTime());
            listVo.setIcon(userEntity.getIcon());
            listVo.setNickName(userEntity.getNickName());
            listVos.add(listVo);


        }
        System.out.println(listVos);
        return  R.ok().setData(listVos);

    }


    /**
     * 付款后返回订单消息（订单号，商品消息，key）
     */
    @GetMapping("/orderItem")
    public PayOrder orderItem(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(requestAttributes);
        UserDTO userDTO = UserHolder.getUser();
        System.out.println("sdajkdha :"+ userDTO);
        List<OrderDetailVo> OrderDetailVos = new ArrayList<>();
        OrderDetailVos = orderFeignService.getOrderItem(userDTO.getId());
        PayOrder payOrder = new PayOrder();
       payOrder.setOrderItems(OrderDetailVos);

        return  payOrder;
    }





    @GetMapping("/ss")
    public void ss() {
        userService.news();
    }

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public R sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 发送短信验证码并保存验证码
        return userService.sendCode(phone, session);
    }

    /**
     * 注册功能
     *
     * @param RegisterForm 注册参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/register")
    public R Register(@RequestBody RegisterFormDTO RegisterForm, HttpSession session) {
        // 实现注册功能

        return userService.register(RegisterForm, session);
    }

    /**
     * 登录功能
     *
     * @param loginForm 注册参数，包含手机号、验证码、密码
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        // 实现登录功能
        System.out.println(loginForm);
        return userService.login(loginForm, session);
    }

    /**
     * 上传头像
     */
    @ResponseBody
    @RequestMapping("/uploadB")
    public Object uploadB(@RequestPart("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            /*得到上传时的文件名*/
            String fileName = file.getOriginalFilename();

            String filePath = "E:\\picture\\";
//            String filePath = "C:/Users/Administrator/uploadFile/file/";
            String path = filePath + fileName;
            File dest = new File(new File(path).getAbsolutePath());
//            System.out.println(dest);

            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            /*springmvc封装的方法，用于图片上传时，把内存中图片写入磁盘*/
            file.transferTo(dest);
//            System.out.println(getFileExtension(new File(fileName)));
            String name = getFileExtension(new File(fileName));

            UserDTO userDTO = UserHolder.getUser();
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id",userDTO.getId());
            updateWrapper.set("icon","http://forum.kakaluote.ltd/image/"+fileName);
            int update = userDao.update(null,updateWrapper);


            return "上传成功";


//            iUploadService.set(fileName,getFileExtension(new File(fileName)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败";


    }



    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("user:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:user:save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //  @RequiresPermissions("user:user:update")
    public R update(@RequestBody UserEntity user) {
        userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:user:delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
