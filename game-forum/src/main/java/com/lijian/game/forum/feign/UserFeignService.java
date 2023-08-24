package com.lijian.game.forum.feign;

import com.lijian.game.common.utils.R;
import com.lijian.game.forum.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lenovo
 */
@FeignClient(name = "game-user",configuration = FeignConfig.class)
public interface UserFeignService {
    /**
     * 信息
     */
    @RequestMapping("user/user/info/{id}")
    R info(@PathVariable("id") Long id);

}
