package com.willowleaf.orderpull.core;

import com.willowleaf.commons.model.Order;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 订单拉取的定时任务。
 *
 * @author dengb
 */
public class OrderPullJob extends QuartzJobBean {

    private OrderPuller puller;
    private OrderPusher pusher;

    OrderPullJob(OrderPuller puller, OrderPusher pusher) {
        this.puller = puller;
        this.pusher = pusher;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        List<Order> orders = puller.pullAndSave();

        pusher.push(orders);
    }
}
