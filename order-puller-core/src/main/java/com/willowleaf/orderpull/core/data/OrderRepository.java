package com.willowleaf.orderpull.core.data;

import com.willowleaf.orderpull.core.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
