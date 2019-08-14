package com.willowleaf.orderpull.core.job;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.OrderPusher;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.model.Order;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 订单拉取的定时任务。
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
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        OrderPuller.Strategy strategy = (OrderPuller.Strategy) jobDataMap.get("strategy");
        List<Order> orders = puller.pullAndSave(strategy);
        pusher.push(orders);
    }
}
