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

    /**
     * 拉取订单数据并保存拉取日志。
     */
    public void pullAndProcess() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startTime = timeInterval.getStartTime(getOrderChannel()).format(formatter);
        String endTime = timeInterval.getEndTime(getOrderChannel()).format(formatter);
        log.info("拉取{}订单信息 [{} - {})", getOrderChannel().getName(), startTime, endTime);

        jdbcTemplate.setFetchSize(Integer.MIN_VALUE);
        jdbcTemplate.query("select * from " + jobProperties.getOrderTableName()
                        + " where created_time >= '" + startTime
                        + "' and created_time < '" + endTime + "'",
                orderRS -> {
                    List<Item> items = jdbcTemplate.query("select * from " + jobProperties.getItemTableName() +
                                    " where order_id = " + orderRS.getLong("id"),
                            this::mapItem);

                    Order order = mapOrder(orderRS);
                    order.setItems(items);
                    processData(order);
                });

        OperationLog operationLog = new OperationLog(timeInterval.getEndTime(getOrderChannel()), getOrderChannel());
        operationRepository.save(operationLog);
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

    /**
     * 处理订单数据。
     *
     * @param order 订单信息
     */
    protected abstract void processData(Order order);
}
