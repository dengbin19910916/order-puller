package com.willowleaf.orderpull.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    /**
     * 返回最后的操作信息。
     *
     * @return 操作信息，可能为空
     */
    @Query("select o from Operation o where o.operationTime = (select max(operationTime) from Operation)")
    Optional<Operation> findLastOne();
}
