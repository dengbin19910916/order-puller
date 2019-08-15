package com.willowleaf.orderpull.core.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Item implements Serializable {

    private Long id;

    private Long orderId;

    private String name;

    private Integer count;
}
