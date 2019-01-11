package com.willowleaf.tmallpull;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.model.Goods;
import com.willowleaf.orderpull.core.model.Order;
import com.willowleaf.orderpull.core.model.Platform;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TmallOrderPuller extends OrderPuller {

    @Override
    protected List<Order> pull(TimeInterval timeInterval) {
        System.out.println("拉取天猫订单，开始时间：" + timeInterval.getStartTime() + "，结束时间：" + timeInterval.getEndTime() + "。");
        Order order1 = new Order();
        order1.setId(1000000000000001L);
        Goods goods1 = new Goods();
        goods1.setId(2000000000000001L);
        order1.setGoods(Collections.singletonList(goods1));

        Order order2 = new Order();
        order2.setId(1000000000000002L);
        Goods goods2 = new Goods();
        goods2.setId(2000000000000002L);
        order2.setGoods(Collections.singletonList(goods2));

        return Arrays.asList(order1, order2);
    }

    @Override
    protected Platform getPlatform() {
        return Platform.ALIBABA;
    }
}
