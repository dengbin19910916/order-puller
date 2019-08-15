package com.willowleaf.tmallpull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.model.Item;
import com.willowleaf.orderpull.core.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class TmallOrderPuller extends OrderPuller<Order> {

    @Override
    public Order.Channel getOrderChannel() {
        return Order.Channel.TMALL;
    }

    @Override
    protected int getTotalPages() {
        return ThreadLocalRandom.current().nextInt(1, 10);
    }

    @Override
    protected List<Order> getData(Strategy strategy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("拉取天猫订单：开始时间[{}]，结束时间[{}]，总共{}页，第{}页，{}条数据。",
                strategy.getStartTime().format(formatter),
                strategy.getEndTime().format(formatter),
                strategy.getTotalPages(),
                strategy.getPage() + 1,
                strategy.getSize());
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
}
