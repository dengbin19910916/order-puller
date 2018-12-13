package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.Order;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将标准订单推送至RabbitMQ。
 *
 * @author dengb
 */
@Component
public class OrderPusher {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public OrderPusher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    void push(List<Order> orders) {
        if (orders.size() > 0) {
            amqpTemplate.convertAndSend(orders);
        }
    }
}
