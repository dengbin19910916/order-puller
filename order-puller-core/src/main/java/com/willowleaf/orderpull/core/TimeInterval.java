package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.model.Order;

import java.time.LocalDateTime;

/**
 * 拉取订单所使用的策略。
 */
public interface TimeInterval {

    /**
     * 返回开始时间，时间区间[startTime, endTime)。
     *
     * @return 开始时间，包含
     */
    LocalDateTime getStartTime(Order.Channel channel);

    /**
     * 返回结束时间，时间区间[startTime, endTime)。
     *
     * @return 结束时间，不包含
     */
    LocalDateTime getEndTime(Order.Channel channel);
}
