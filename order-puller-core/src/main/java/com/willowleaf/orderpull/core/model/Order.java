package com.willowleaf.orderpull.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Order implements Serializable {

    private Long id;

    private List<Item> items;

    public enum Channel {

        TMALL {
            @Override
            public String getName() {
                return "天猫";
            }
        },
        JD {
            @Override
            public String getName() {
                return "京东";
            }
        },
        VIP {
            @Override
            public String getName() {
                return "唯品会";
            }
        };

        /**
         * 返回渠道名称。
         *
         * @return 渠道名称
         */
        public abstract String getName();
    }
}
