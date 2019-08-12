package com.willowleaf.tmallpull;

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
public class TmallOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(TimeInterval timeInterval) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("拉取天猫订单，开始时间：" + timeInterval.getStartTime(getOrderChannel()).format(formatter)
                + "，结束时间：" + timeInterval.getEndTime(getOrderChannel()).format(formatter) + "。");
        Order order1 = new Order();
        order1.setId("TMALL_ORDER-1");
        Item item1 = new Item();
        item1.setId("TMALL_GOODS-1");
        order1.setItems(Collections.singletonList(item1));

        Order order2 = new Order();
        order2.setId("TMALL_ORDER-2");
        Item item2 = new Item();
        item2.setId("TMALL_GOODS-2");
        order2.setItems(Collections.singletonList(item2));

        return Arrays.asList(order1, order2);
    }

    @Override
    protected Order.Channel getOrderChannel() {
        return Order.Channel.TMALL;
    }
}
