package com.willowleaf.jdpull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.model.Item;
import com.willowleaf.orderpull.core.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JdOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(Strategy strategy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("拉取京东订单：开始时间[{}]，结束时间[{}]，第{}页，{}条数据。",
                strategy.getTimeInterval().getStartTime(getOrderChannel()).format(formatter),
                strategy.getTimeInterval().getEndTime(getOrderChannel()).format(formatter),
                strategy.getPage() + 1, strategy.getSize());
        Order order1 = new Order();
        order1.setId("JD_ORDER-1");
        Item item1 = new Item();
        item1.setId("JD_GOODS-1");
        order1.setItems(Collections.singletonList(item1));

        Order order2 = new Order();
        order2.setId("JD_ORDER-2");
        Item item2 = new Item();
        item2.setId("JD_GOODS-2");
        order2.setItems(Collections.singletonList(item2));

        return Arrays.asList(order1, order2);
    }

    @Override
    protected Order.Channel getOrderChannel() {
        return Order.Channel.TMALL;
    }
}
