package com.lijian.game.product.service.impl;

import com.lijian.game.product.entity.BrandEntity;
import com.lijian.game.product.entity.SkuEntity;
import com.lijian.game.product.service.BrandService;
import com.lijian.game.product.service.SkuService;
import com.lijian.game.product.vo.RecommendVo;
import org.springframework.beans.BeanUtils;
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

import com.lijian.game.product.dao.SpuDao;
import com.lijian.game.product.entity.SpuEntity;
import com.lijian.game.product.service.SpuService;


@Service("spuService")
public class SpuServiceImpl extends ServiceImpl<SpuDao, SpuEntity> implements SpuService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private BrandService brandService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuEntity> page = this.page(
                new Query<SpuEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<RecommendVo> queryRecommend() {
        List<SkuEntity> skuEntity = skuService.list();
        List<RecommendVo> list = skuEntity.stream().map(sku -> {
            Long spuId = sku.getSpuId();
            SpuEntity spuEntity = this.baseMapper.selectOne(new QueryWrapper<SpuEntity>().select("sub_title").eq("id", spuId));
            RecommendVo recommendVo = new RecommendVo();
            BeanUtils.copyProperties(sku, recommendVo);
            recommendVo.setSubTitle(spuEntity.getSubTitle());
            return  recommendVo;
        }).collect(Collectors.toList());
        return  list;
    }

    @Override
    public SpuEntity getSpuInfoBySkuId(Long skuId) {
        //先查询sku表里的数据
        SkuEntity skuEntity = skuService.getById(skuId);
        //获得spuId
        Long spuId = skuEntity.getSpuId();
        //再通过spuId查询spuInfo信息表里的数据
        SpuEntity spuEntity = this.baseMapper.selectById(spuId);


        return spuEntity;
    }

}
