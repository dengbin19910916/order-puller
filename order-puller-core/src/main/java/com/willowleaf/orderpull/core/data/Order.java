package com.willowleaf.orderpull.core.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 标准订单。
 *
 * @author dengb
 */
@Data
@Entity
@Table(name = "`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = -374594897159418502L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order")
    private List<Goods> goods;

    @ManyToOne
    private Operation operation;
}
