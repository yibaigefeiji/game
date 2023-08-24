package com.lijian.game.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.product.entity.KeyEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-25 10:52:11
 */
@Service
public interface KeyService extends IService<KeyEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 根据skuId查询key的信息
     * @param skuId
     * @return
     */
    KeyEntity getBySku(Long skuId);
}

