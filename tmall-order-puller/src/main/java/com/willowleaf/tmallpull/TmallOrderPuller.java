package com.willowleaf.tmallpull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.Timer;
import com.willowleaf.orderpull.core.data.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TmallOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(Timer timer) {
        System.out.println("拉取天猫订单，开始时间：" + timer.getStartTime() + "，结束时间：" + timer.getEndTime());
        return Collections.emptyList();
    }
}
