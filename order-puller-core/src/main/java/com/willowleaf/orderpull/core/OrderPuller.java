package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.OperationRepository;
import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 定时拉取平台的订单报文。
 * 由子类实现如何拉取订单报文。
 *
 * @see OrderPuller#pull(TimeInterval) 拉取订单报文
 */
@Getter
public abstract class OrderPuller {

    @Autowired
    protected TimeInterval timeInterval;
    @Autowired
    protected OperationRepository operationRepository;

    /**
     * 拉取订单数据并保存拉取日志。
     *
     * @return 订单信息列表
     */
    public List<Order> pullAndSave() {
        List<Order> orders = pull(timeInterval);
        OperationLog operationLog = new OperationLog(timeInterval.getEndTime(getOrderChannel()), getOrderChannel());
        operationRepository.save(operationLog);
        return orders;
    }

    /**
     * 拉取平台的订单报文数据。
     *
     * @param timeInterval 定时器
     * @return 订单报文列表
     */
    protected abstract List<Order> pull(TimeInterval timeInterval);

    /**
     * 返回订单的来源渠道。
     *
     * @return 渠道类型
     */
    protected abstract Order.Channel getOrderChannel();
}
