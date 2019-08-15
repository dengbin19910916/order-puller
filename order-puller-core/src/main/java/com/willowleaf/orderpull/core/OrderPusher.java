package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;

import static com.willowleaf.orderpull.core.job.JobAutoConfiguration.ORDER_QUEUE_NAME;

/**
 * 将标准订单推送至RabbitMQ。
 */
@Slf4j
@Component
public class OrderPusher implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private final AmqpTemplate amqpTemplate;

    public OrderPusher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void push(List<?> orders) {
        if (!ObjectUtils.isEmpty(orders)) {
            amqpTemplate.convertAndSend(ORDER_QUEUE_NAME, orders);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.error("RabbitMQ confirm error. CorrelationData: {}\nAck:{}\nCause:{}", correlationData, ack, cause);
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("RabbitMQ returned message error. Message: {}\nReplyCode: {}\nReplyText: {}\nExchange: {}\nRoutingKey: {}",
                message, replyCode, replyText, exchange, routingKey);
    }
}
