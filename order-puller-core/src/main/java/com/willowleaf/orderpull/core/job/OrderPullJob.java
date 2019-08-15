package com.willowleaf.orderpull.core.job;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.OrderPusher;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import java.util.List;

/**
 * 订单拉取的定时任务。
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderPullJob extends MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob {

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
        List<?> orders = puller.pullAndSave(strategy);
        pusher.push(orders);
    }
}
