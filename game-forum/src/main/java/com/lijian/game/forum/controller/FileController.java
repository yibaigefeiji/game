package com.lijian.game.forum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".")+1);
        } else {
            return "";
        }
    }


    @ResponseBody
    @RequestMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) {
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


            return "上传成功";


//            iUploadService.set(fileName,getFileExtension(new File(fileName)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败";


    }
}
