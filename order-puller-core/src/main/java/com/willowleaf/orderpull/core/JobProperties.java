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
     * 订单拉取任务的开始时间.
     */
    private String startTime;
    /**
     * 时间间隔（秒）。
     */
    private int timeInterval = 300;
    /**
     * 任务名称.
     */
    private String jobIdentity = "order-pull-job";

    public LocalDateTime getStartTime() {
        return startTime == null ? null : LocalDateTime.parse(startTime);
    }
}
