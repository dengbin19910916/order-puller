package com.willowleaf.orderpull.core.job;

import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.data.OperationRepository;
import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 基于数据库实现的时间区间。
 */
public class JdbcTimeInterval implements TimeInterval {

    private final OperationRepository operationRepository;
    private final JobProperties jobProperties;

    JdbcTimeInterval(OperationRepository operationRepository,
                     JobProperties jobProperties) {
        this.operationRepository = operationRepository;
        this.jobProperties = jobProperties;
    }

    @Override
    public LocalDateTime getStartTime(Order.Channel channel) {
        Optional<OperationLog> lastOperation = operationRepository.findLastOperateTime(channel);
        return lastOperation.isPresent() ? lastOperation.get().getOperationTime() :
                jobProperties.getStartTime();
    }

    @Override
    public LocalDateTime getEndTime(Order.Channel channel) {
        return getStartTime(channel).plusSeconds(jobProperties.getTimeInterval());
    }
}
