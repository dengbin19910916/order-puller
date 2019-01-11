package com.willowleaf.orderpull.core.model;

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

    @Id
    private Long id;

    @OneToMany(mappedBy = "order")
    private List<Goods> goods;
}
