package com.willowleaf.orderpull.core;

import com.alibaba.fastjson.JSON;
import com.willowleaf.orderpull.core.data.OperationRepository;
import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;
import jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时拉取平台的订单报文。
 * 由子类实现如何拉取订单报文。
 *
 * @see OrderPuller#pull(Strategy) 拉取订单报文
 */
public abstract class OrderPuller {

    @Autowired
    protected OperationRepository operationRepository;

    /**
     * 拉取订单数据并保存拉取日志。
     *
     * @return 订单信息列表
     */
    @Transactional
    public List<Order> pullAndSave(Strategy strategy) {
        List<Order> orders = pull(strategy);
        OperationLog operationLog = new OperationLog(
                strategy.getTimeInterval().getEndTime(getOrderChannel()),
                getOrderChannel(),
                strategy.getPage(),
                strategy.getSize(),
                JSON.toJSONString(orders)
        );
        operationRepository.save(operationLog);

        // TODO 在这里控制拉取订单的策略
        updateStrategy(strategy);
        return orders;
    }

    private void updateStrategy(Strategy strategy) {
        strategy.setPage(strategy.getPage() + 1);
    }

    /**
     * 拉取平台的订单报文数据。
     *
     * @param strategy 拉取
     * @return 订单报文列表
     */
    protected abstract List<Order> pull(Strategy strategy);

    /**
     * 返回订单的来源渠道。
     *
     * @return 渠道类型
     */
    protected abstract Order.Channel getOrderChannel();

    /**
     * 拉取订单所使用的策略。
     */
    @Data
    @AllArgsConstructor
    public static class Strategy {
        /**
         * 时间区间。
         */
        private TimeInterval timeInterval;

        /**
         * 分页页数。
         */
        private int page;

        /**
         * 每页的条目数。
         */
        private int size;
    }
}
