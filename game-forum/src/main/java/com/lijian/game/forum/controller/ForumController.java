package com.lijian.game.forum.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.TypeReference;
import com.lijian.game.forum.DTO.UserDTO;
import com.lijian.game.forum.feign.ProductFeignService;
import com.lijian.game.forum.feign.UserFeignService;
import com.lijian.game.forum.utils.UserHolder;
import com.lijian.game.forum.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.lijian.game.forum.entity.ForumEntity;
import com.lijian.game.forum.service.ForumService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2023-02-23 21:16:47
 */
@RestController
@RequestMapping("forum/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;
    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".")+1);
        } else {
            return "";
        }
    }


    /**
     * 根据用户id查询所发布的文章
     */
    @RequestMapping("ArticleById")
    public R ArticleById(@RequestParam("id") Long id){
      List<ForumEntity> forumEntityList = forumService.ArticleById(id);

      return R.ok().setData(forumEntityList);


    }











    /**
     * 点赞功能
     */
    @RequestMapping("likes")
    public R likes(@RequestParam("id") Long id){
        int r = forumService.likes(id);

        return R.ok().setData(r);
    }


    /**
     * 文章列表
     */
    @RequestMapping("ArticleList")
    public R ArticleList(){
        List<ForumEntity> forumEntityList = forumService.list();
        List<listVo> listVos = new ArrayList<>();
//        for (int i = 0; i < forumEntityList.size(); i++) {
//            listVo listVo = new listVo();
//            UserVo userVo = userFeignService.info(forumEntityList.get(i).getUserInfo())
//                    .getData("data",new TypeReference<UserVo>(){});
//            listVo.setId(forumEntityList.get(i).getId());
//            listVo.setComment(forumEntityList.get(i).getComment());
//            listVo.setGameName(forumEntityList.get(i).getGameName());
//            listVo.setGameSpu(forumEntityList.get(i).getGameSpu());
//            listVo.setImages(forumEntityList.get(i).getImages());
//            listVo.setLikes(forumEntityList.get(i).getLikes());
//            listVo.setLable(forumEntityList.get(i).getLable());
//            listVo.setUserInfo(forumEntityList.get(i).getUserInfo());
//            listVo.setText(forumEntityList.get(i).getText());
//            listVo.setTitle(forumEntityList.get(i).getTitle());
//            listVo.setTime(forumEntityList.get(i).getTime());
//            listVo.setIcon(userVo.getIcon());
//            listVo.setNickName(userVo.getNickName());
//            listVos.add(listVo);
//
//
//        }


        return R.ok().setData(forumEntityList);
    }

    /**
     * 发布文章
     */
    @RequestMapping("/PostArticle")
    public R PostArticle(@RequestParam Map<String,Object> params){
        System.out.println(params);
        R r = forumService.postArticle(params);

        return R.ok("ok");
    }

    /**
     * 标签
     */
    @RequestMapping("/listGame")
    public R listGame(){
        List<spuVo> spuVos = productFeignService.listA().getData("data",new TypeReference<List<spuVo>>(){});
        System.out.println(spuVos);
        List<actionsVo> actionsVos = new ArrayList<>();
        for (int i = 0; i < spuVos.size(); i++) {
                actionsVo actionsVo = new actionsVo();
                actionsVo.setName(spuVos.get(i).getName());
                actionsVo.setSpuId((spuVos.get(i).getId()));
                actionsVos.add(actionsVo);
        }
        return R.ok().setData(actionsVos);
    }


    @ResponseBody
    @RequestMapping("/uploadA")
    public Object uploadA(@RequestPart("file") MultipartFile file) {
        log.info("请求成功");
        log.info(String.valueOf(file));
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            /*得到上传时的文件名*/
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名:" + fileName);
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

            FileVo fileVo = new FileVo();
            fileVo.setErrno(0);
            data data = new data();
            data.setAlt("1");
            data.setHref("");
            data.setUrl("http://forum.kakaluote.ltd/image/"+fileName);
            fileVo.setData(data);
            return "http://forum.kakaluote.ltd/image/"+fileName;


//            iUploadService.set(fileName,getFileExtension(new File(fileName)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败";


    }



    @ResponseBody
    @RequestMapping("/upload")
    public Object upload(@RequestParam("wangeditor-uploaded-image") MultipartFile file) {
        log.info("请求成功");
        log.info(String.valueOf(file));
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            /*得到上传时的文件名*/
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名:" + fileName);
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

            FileVo fileVo = new FileVo();
            fileVo.setErrno(0);
            data data = new data();
            data.setAlt("1");
            data.setHref("");
            data.setUrl("http://forum.kakaluote.ltd/image/"+fileName);
            fileVo.setData(data);
            return fileVo;


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
   // @RequiresPermissions("forum:forum:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = forumService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("forum:forum:info")
    public R info(@PathVariable("id") Long id){
		ForumEntity forum = forumService.getById(id);

        return R.ok().put("forum", forum);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("forum:forum:save")
    public R save(@RequestBody ForumEntity forum){
		forumService.save(forum);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
  //  @RequiresPermissions("forum:forum:update")
    public R update(@RequestBody ForumEntity forum){
		forumService.updateById(forum);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("forum:forum:delete")
    public R delete(@RequestBody Long[] ids){
		forumService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
