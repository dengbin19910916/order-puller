package com.willowleaf.orderpull.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Id
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
    @JsonIgnore
    private Order order;
}
