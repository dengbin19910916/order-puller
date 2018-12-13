package com.willowleaf.orderpull.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDateTime;

/**
 * 拉取订单任务的配置属性。
 *
 * @author dengb
 */
@Data
@ConfigurationProperties(prefix = "job.pull")
public class JobProperties {
    /**
     * The first time to start.
     */
    private String firstTime;
    /**
     * Time interval (in seconds), it recommends more than five seconds.
     */
    private int timeInterval = 5;
    /**
     * Job identity.
     */
    private String jobIdentity = "order-pull-job";

    public LocalDateTime getFirstTime() {
        return firstTime == null ? null : LocalDateTime.parse(firstTime);
    }
}
