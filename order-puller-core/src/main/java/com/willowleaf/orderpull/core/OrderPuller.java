package com.willowleaf.orderpull.core;

import com.alibaba.fastjson.JSON;
import com.willowleaf.orderpull.core.data.OperationRepository;
import com.willowleaf.orderpull.core.job.JobProperties;
import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 定时拉取平台的订单报文。
 * 由子类实现如何拉取订单报文。
 *
 * @see OrderPuller#getData(Strategy) 拉取订单报文
 */
@Slf4j
public abstract class OrderPuller<T extends Serializable> {

    @Autowired
    protected OperationRepository operationRepository;
    @Autowired
    protected Scheduler scheduler;

    /**
     * 拉取订单数据并保存拉取日志。
     *
     * @return 订单信息列表
     */
    @SneakyThrows
    public List<T> pullAndSave(Strategy strategy) {
        LocalDateTime startTime = strategy.getTimeInterval().getStartTime(getOrderChannel());
        LocalDateTime endTime = strategy.getTimeInterval().getEndTime(getOrderChannel());
        OperationLog operationLog = new OperationLog(
                endTime,
                getOrderChannel(),
                getTotalPages(),
                strategy.getPage(),
                strategy.getSize()
        );
        operationLog = operationRepository.save(operationLog);
        strategy.setTotalPages(getTotalPages());

        List<T> orders = Collections.emptyList();
        while (hasNext(strategy)) {
            strategy.setStartTime(startTime);
            strategy.setEndTime(endTime);
            try {
                // TODO 模拟执行会超过时间区间
                int time = ThreadLocalRandom.current().nextInt(3, 15);
                TimeUnit.SECONDS.sleep(time);
                orders = getData(strategy);
                operationLog.setMessage(JSON.toJSONString(orders));
                operationRepository.save(operationLog);
                log.error("===>>> 时间[{}, {}]的事情终于做完了！耗时：{}", startTime, endTime, time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO 在这里控制拉取订单的策略
            updateStrategy(strategy);
        }

        return orders;
    }

    private void updateStrategy(Strategy strategy) {
        strategy.setPage(strategy.getPage() + 1);
    }

    /**
     * 返回订单的来源渠道。
     *
     * @return 渠道类型
     */
    public abstract Order.Channel getOrderChannel();

    protected abstract int getTotalPages();

    /**
     * 是否存在更多数据。
     *
     * @return true - 存在，false - 不存在。
     */
    protected boolean hasNext(Strategy strategy) {
        return strategy.totalPages == -1 || (strategy.page++ < strategy.totalPages);
    }

    /**
     * 拉取并返回平台的订单报文数据。
     *
     * @param strategy 拉取
     * @return 订单报文列表
     */
    protected abstract List<T> getData(Strategy strategy);

    /**
     * 拉取订单所使用的策略。
     */
    @Data
    public static class Strategy {
        /**
         * 时间区间。
         */
        private TimeInterval timeInterval;
        /**
         * 开始时间。
         */
        private LocalDateTime startTime;
        /**
         * 结束时间。
         */
        private LocalDateTime endTime;
        /**
         * 总页数。
         */
        private int totalPages = -1;
        /**
         * 第几页。
         */
        private int page;
        /**
         * 每页的条目数。
         */
        private int size;

        public Strategy(TimeInterval timeInterval, int size) {
            this.timeInterval = timeInterval;
            this.size = size;
        }
    }
}
