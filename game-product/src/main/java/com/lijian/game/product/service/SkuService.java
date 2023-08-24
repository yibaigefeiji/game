package com.lijian.game.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.product.entity.SkuEntity;
import com.lijian.game.product.vo.DataVo;
import com.lijian.game.product.vo.GameDetialVo;
import com.lijian.game.product.vo.LabelVo;
import com.lijian.game.product.vo.ListVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@Service
public interface SkuService extends IService<SkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 游戏查询级按价格和销量升降序
     *
     */
    List<SkuEntity> search(Map<String, Object> params);
    /**
     * 按游戏标签分类查找游戏形成分类列表
     */
    List<ArrayList<List<ListVo>>> label();
    /**
     * 猜你喜欢
     */
    List<SkuEntity> like();
    /**
     * 游戏详情页
     */
    List<GameDetialVo> gameDetailById(Long id) throws ExecutionException, InterruptedException;
}

