package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.Operation;
import com.willowleaf.orderpull.core.data.OperationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 基于数据库实现的定时器。
 *
 * @author dengb
 */
public class JdbcIntervalTimer implements Timer {

    private final OperationRepository operationRepository;
    private final JobProperties jobProperties;

    public JdbcIntervalTimer(OperationRepository operationRepository,
                             JobProperties jobProperties) {
        this.operationRepository = operationRepository;
        this.jobProperties = jobProperties;
    }

    @Override
    public LocalDateTime getStartTime() {
        Optional<Operation> lastOperation = operationRepository.findLastOne();
        return lastOperation.isPresent() ? lastOperation.get().getOperationTime() :
                jobProperties.getFirstTime() != null ? jobProperties.getFirstTime() : LocalDateTime.now();
    }

    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plusSeconds(jobProperties.getTimeInterval());
    }
}
