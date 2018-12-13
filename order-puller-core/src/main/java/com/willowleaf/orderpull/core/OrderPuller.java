package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时拉取平台的订单报文。
 * 由子类实现如何拉取订单报文。
 *
 * @author dengb
 * @see OrderPuller#pull(Timer)  拉取订单报文
 */
@Getter
public abstract class OrderPuller {

    @Autowired
    protected Timer timer;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected GoodsRepository goodsRepository;
    @Autowired
    protected OperationRepository operationRepository;

    /**
     * 拉取订单数据并保存。
     *
     * @return 订单信息列表
     */
    @Transactional
    public List<Order> pullAndSave() {
        List<Order> orders = pull(timer);
        Operation operation = new Operation(timer.getEndTime(), orders);
        operationRepository.save(operation);

        if (orders != null && orders.size() > 0) {
            orders.forEach(order -> {
                order.setOperation(operation);
                orderRepository.save(order);

                order.getGoods().forEach(goods -> {
                    goods.setOrder(order);
                    goodsRepository.save(goods);
                });
            });
        }
        return orders;
    }

    /**
     * 拉取平台的订单报文数据。
     *
     * @param timer 定时器
     * @return 订单报文列表
     */
    protected abstract List<Order> pull(Timer timer);
}
