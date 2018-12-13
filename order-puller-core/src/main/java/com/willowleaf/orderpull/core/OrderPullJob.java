package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.Order;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 订单拉取的Quartz任务。
 *
 * @author dengb
 */
public class OrderPullJob extends QuartzJobBean {

    @Autowired
    private OrderPuller puller;
    @Autowired
    private  OrderPusher pusher;

    /**
     * 执行定时任务
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        List<Order> orders = puller.pullAndSave();

        pusher.push(orders);
    }
}
