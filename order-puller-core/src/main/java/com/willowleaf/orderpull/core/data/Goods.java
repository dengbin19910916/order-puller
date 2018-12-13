package com.willowleaf.orderpull.core.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品。
 *
 * @author dengb
 */
@Data
@Entity
public class Goods implements Serializable {

    private static final long serialVersionUID = -2272867488265027683L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 商品名称。
     */
    private String name;
    /**
     * 商品价格。
     */
    private BigDecimal price;
    /**
     * 所属订单。
     */
    @ManyToOne
    private Order order;
}
