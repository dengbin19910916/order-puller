package com.willowleaf.orderpull.core.data;

import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<OperationLog, Long> {

    /**
     * 返回最后的操作信息。
     *
     * @return 操作信息，可能为空
     */
    @Query("select o from OperationLog o where o.operationTime = (select max(operationTime) from OperationLog where platform = ?1) and platform = ?1")
    Optional<OperationLog> findLastOperateTime(Platform platform);
}
