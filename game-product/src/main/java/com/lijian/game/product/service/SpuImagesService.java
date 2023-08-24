package com.lijian.game.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.product.entity.SpuImagesEntity;
import com.lijian.game.product.vo.AddImagesVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-06 15:45:15
 */
@Service
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(AddImagesVo spuImages);

    void saveImage(Long spuId, String imgName, List<String> images);
}

