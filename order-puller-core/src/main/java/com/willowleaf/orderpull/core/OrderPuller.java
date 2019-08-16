package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.OperationRepository;
import com.willowleaf.orderpull.core.job.JobProperties;
import com.willowleaf.orderpull.core.model.Item;
import com.willowleaf.orderpull.core.model.OperationLog;
import com.willowleaf.orderpull.core.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 定时拉取平台的订单报文。
 * 由子类实现如何拉取订单报文。
 *
 * @see OrderPuller#pullAndProcess() 拉取订单报文
 */
@Slf4j
public abstract class OrderPuller {

    @Autowired
    protected TimeInterval timeInterval;
    @Autowired
    protected JobProperties jobProperties;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected OperationRepository operationRepository;

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 拉取订单数据并保存拉取日志。
     */
    public void pullAndProcess() {
        LocalDateTime startTime = timeInterval.getStartTime(getOrderChannel());
        LocalDateTime endTime = timeInterval.getEndTime(getOrderChannel());
        log.info("拉取{}订单信息 [{} - {})", getOrderChannel().getName(), startTime, endTime);

        pullAndProcess(startTime, endTime, jobProperties.getSize(), this::processData);

        OperationLog operationLog = new OperationLog(endTime, getOrderChannel());
        operationRepository.save(operationLog);
    }

    protected void pullAndProcess(LocalDateTime startTime, LocalDateTime endTime, int pageSize, Processor processor) {
        pullData(startTime, endTime, pageSize).parallelStream().forEach(processor::process);
    }

    /**
     * 返回订单的来源渠道。
     *
     * @return 渠道类型
     */
    public abstract Order.Channel getOrderChannel();

    /**
     * 转换订单信息。
     *
     * @param rs 结果集
     * @return 订单信息
     * @throws SQLException 不用捕获SQL异常
     */
    protected abstract Order mapOrder(ResultSet rs) throws SQLException;

    /**
     * 转换订单明细信息。
     *
     * @param rs 结果集
     * @return 订单明细信息
     * @throws SQLException 不用捕获SQL异常
     */
    protected abstract Item mapItem(ResultSet rs, int rowNum) throws SQLException;

    protected abstract List<Order> pullData(LocalDateTime startTime, LocalDateTime endTime, int pageSize);

    /**
     * 处理订单数据。
     *
     * @param order 订单信息
     */
    protected abstract void processData(Order order);

    @FunctionalInterface
    public interface Processor {
        /**
         * 处理订单数据。
         *
         * @param order 订单信息
         */
        void process(Order order);
    }
}
