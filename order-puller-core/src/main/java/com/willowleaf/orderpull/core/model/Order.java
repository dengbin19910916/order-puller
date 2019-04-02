package com.willowleaf.orderpull.core.model;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private String id;

    private List<Goods> goods;
}
