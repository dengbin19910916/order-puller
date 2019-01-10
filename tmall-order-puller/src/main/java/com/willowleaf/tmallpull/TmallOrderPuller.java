package com.willowleaf.tmallpull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.model.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TmallOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(TimeInterval timeInterval) {
        System.out.println("拉取天猫订单，开始时间：" + timeInterval.getStartTime() + "，结束时间：" + timeInterval.getEndTime() + "。");
        return Collections.emptyList();
    }
}
