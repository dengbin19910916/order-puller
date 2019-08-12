package com.willowleaf.orderpull.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Order implements Serializable {

    private String id;

    private List<Item> items;

    public enum Channel {

        TMALL,
        JD,
        VIP
    }
}
