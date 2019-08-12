package com.willowleaf.vippull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.model.Item;
import com.willowleaf.orderpull.core.model.Order;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class VipOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(TimeInterval timeInterval) {
        System.out.println("拉取唯品会订单，开始时间：" + timeInterval.getStartTime(getOrderChannel()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + "，结束时间：" + timeInterval.getEndTime(getOrderChannel()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "。");
        Order order1 = new Order();
        order1.setId("VIP_ORDER-1");
        Item item1 = new Item();
        item1.setId("VIP_GOODS-1");
        order1.setItems(Collections.singletonList(item1));

        Order order2 = new Order();
        order2.setId("VIP_ORDER-2");
        Item item2 = new Item();
        item2.setId("VIP_GOODS-2");
        order2.setItems(Collections.singletonList(item2));

        return Arrays.asList(order1, order2);
    }

    @Override
    protected Order.Channel getOrderChannel() {
        return Order.Channel.VIP;
    }
}
