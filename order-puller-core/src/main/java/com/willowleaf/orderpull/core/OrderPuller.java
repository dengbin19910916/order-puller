package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.*;
import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;
import com.willowleaf.orderpull.core.model.Platform;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时拉取平台的订单报文。
 * 由子类实现如何拉取订单报文。
 *
 * @author dengb
 * @see OrderPuller#pull(TimeInterval) 拉取订单报文
 */
@Getter
public abstract class OrderPuller {

    @Autowired
    protected TimeInterval timeInterval;
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
        List<Order> orders = pull(timeInterval);
        OperationLog operationLog = new OperationLog(timeInterval.getEndTime(getPlatform()), getPlatform());
        operationRepository.save(operationLog);

        if (orders != null && orders.size() > 0) {
            orders.forEach(order -> {
                order.getGoods().forEach(goods -> goodsRepository.save(goods));
                orderRepository.save(order);
            });
        }
        return orders;
    }

    /**
     * 拉取平台的订单报文数据。
     *
     * @param timeInterval 定时器
     * @return 订单报文列表
     */
    protected abstract List<Order> pull(TimeInterval timeInterval);

    protected abstract Platform getPlatform();
}
