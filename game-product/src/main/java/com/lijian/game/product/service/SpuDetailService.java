package com.lijian.game.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijian.game.common.utils.PageUtils;
import com.lijian.game.product.entity.SpuDetailEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 *
 *
 * @author lijian
 * @email 2411749208@qq.com
 * @date 2022-09-04 09:48:09
 */
@Service
public interface SpuDetailService extends IService<SpuDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

