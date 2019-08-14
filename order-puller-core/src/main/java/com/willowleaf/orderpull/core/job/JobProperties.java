package com.willowleaf.orderpull.core.job;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 拉取订单任务的配置属性。
 */
@Data
@ConfigurationProperties(prefix = "job")
public class JobProperties {
    /**
     * 订单拉取任务的开始时间。
     */
    private String startTime;
    /**
     * 时间间隔（秒）。
     */
    private int timeInterval = 300;
    /**
     * 任务名称。
     */
    private String jobIdentity;
    /**
     * 每次拉取的条目数。
     */
    private int size = 100;

    public LocalDateTime getStartTime() {
        return startTime == null ? LocalDateTime.now()
                : LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
