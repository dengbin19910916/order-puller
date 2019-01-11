package com.willowleaf.vippull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.model.Goods;
import com.willowleaf.orderpull.core.model.Order;
import com.willowleaf.orderpull.core.model.Platform;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class VipOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(TimeInterval timeInterval) {
        System.out.println("拉取唯品会订单，开始时间：" + timeInterval.getStartTime(getPlatform()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + "，结束时间：" + timeInterval.getEndTime(getPlatform()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "。");
        Order order1 = new Order();
        order1.setId(1000000001L);
        Goods goods1 = new Goods();
        goods1.setId(2000000001L);
        order1.setGoods(Collections.singletonList(goods1));

        Order order2 = new Order();
        order2.setId(1000000002L);
        Goods goods2 = new Goods();
        goods2.setId(2000000002L);
        order2.setGoods(Collections.singletonList(goods2));

        return Arrays.asList(order1, order2);
    }

    @Override
    protected Platform getPlatform() {
        return Platform.VIP;
    }
}
