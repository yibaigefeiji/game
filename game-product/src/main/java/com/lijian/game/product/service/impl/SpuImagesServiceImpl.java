package com.lijian.game.product.service.impl;

import com.lijian.game.product.vo.AddImagesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.common.utils.Query;

import com.lijian.game.product.dao.SpuImagesDao;
import com.lijian.game.product.entity.SpuImagesEntity;
import com.lijian.game.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {
    @Autowired
    SpuImagesService imagesService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(AddImagesVo spuImages) {
        if(spuImages == null){

        }else {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            List<String> images = spuImages.getImgUrl();
            System.out.println(images);
                imagesService.saveImage(spuImages.getSpuId(),spuImages.getImgName(),images);

        }
    }

    @Override
    public void saveImage(Long spuId, String imgName, List<String> images) {
        if(images == null || images.size() == 0){
        }else{
            List<SpuImagesEntity> collect = images.stream().map(img -> {
                SpuImagesEntity spuImages = new SpuImagesEntity();
                spuImages.setSpuId(spuId);
                spuImages.setImgName(imgName);
                spuImages.setImgUrl(img);
                return spuImages;
            }).collect(Collectors.toList());
            this.saveBatch(collect);
        }
    }

}
