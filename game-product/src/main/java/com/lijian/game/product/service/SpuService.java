package com.lijian.game.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.product.entity.SpuEntity;
import com.lijian.game.product.vo.RecommendVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * spu表描述的是一个抽象性的商品，比如 iphone8
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:10
 */
@Service
public interface SpuService extends IService<SpuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 爆款列表展示
     */
    List<RecommendVo> queryRecommend();
    /**
     * 根据skuId查询spu的信息(供远程调用)
     * @param skuId
     * @return
     */
    SpuEntity getSpuInfoBySkuId(Long skuId);
}

