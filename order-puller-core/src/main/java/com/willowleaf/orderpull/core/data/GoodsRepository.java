package com.willowleaf.orderpull.core.data;

import com.willowleaf.orderpull.core.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
