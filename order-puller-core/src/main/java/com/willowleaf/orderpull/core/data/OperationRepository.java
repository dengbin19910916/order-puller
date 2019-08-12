package com.willowleaf.orderpull.core.data;

import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<OperationLog, Long> {

    /**
     * 返回最后的操作信息。
     *
     * @return 操作信息，可能为空
     */
    @Query("select o from OperationLog o where o.operationTime = (select max(operationTime) from OperationLog where channel = ?1) and o.channel = ?1")
    Optional<OperationLog> findLastOperateTime(Order.Channel channel);
}
