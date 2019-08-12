package com.willowleaf.orderpull.core.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Goods implements Serializable {

    private String id;

    private String name;
}
