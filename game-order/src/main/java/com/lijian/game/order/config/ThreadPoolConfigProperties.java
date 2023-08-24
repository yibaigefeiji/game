package com.lijian.game.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: lj
 *
 **/

@ConfigurationProperties(prefix = "game.thread")
// @Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;


}
