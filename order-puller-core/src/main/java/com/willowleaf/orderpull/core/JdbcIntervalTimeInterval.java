package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.data.OperationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 基于数据库实现的定时器。
 *
 * @author dengb
 */
public class JdbcIntervalTimeInterval implements TimeInterval {

    private final OperationRepository operationRepository;
    private final JobProperties jobProperties;

    JdbcIntervalTimeInterval(OperationRepository operationRepository,
                             JobProperties jobProperties) {
        this.operationRepository = operationRepository;
        this.jobProperties = jobProperties;
    }

    @Override
    public LocalDateTime getStartTime() {
        Optional<OperationLog> lastOperation = operationRepository.findLastOperateTime();
        return lastOperation.isPresent() ? lastOperation.get().getOperationTime() :
                jobProperties.getStartTime() != null ? jobProperties.getStartTime() : LocalDateTime.now();
    }

    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plusSeconds(jobProperties.getTimeInterval());
    }
}
