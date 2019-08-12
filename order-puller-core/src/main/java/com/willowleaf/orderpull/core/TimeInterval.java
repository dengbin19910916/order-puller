package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.model.Platform;

import java.time.LocalDateTime;

/**
 * 拉取线上平台订单的定时器。
 * 确定拉取订单的时间区间[startTime, endTime)。
 */
public interface TimeInterval {

    /**
     * 返回时间区间的前端点。
     *
     * @return 开始时间，包含
     */
    LocalDateTime getStartTime(Platform platform);

    /**
     * 返回时间区间的后端点。
     *
     * @return 结束时间，不包含
     */
    LocalDateTime getEndTime(Platform platform);
}
