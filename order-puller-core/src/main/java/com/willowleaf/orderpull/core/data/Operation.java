package com.willowleaf.orderpull.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 拉取操作的信息。
 *
 * @author dengb
 */
@Data
@NoArgsConstructor
@Entity
public class Operation implements Serializable {

    private static final long serialVersionUID = 4145141843018974396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 操作时间，也是上次拉取订单数据的截止时间。
     */
    @Column(nullable = false)
    private LocalDateTime operationTime;
    /**
     * 拉取的订单总数。
     */
    private int count;
    /**
     * 拉取的订单报文。
     */
    @OneToMany(mappedBy = "operation")
    private List<Order> orders;

    public Operation(LocalDateTime operationTime, List<Order> orders) {
        this.operationTime = operationTime;
        this.count = orders == null ? 0 : orders.size();
        this.orders = orders;
    }
}
