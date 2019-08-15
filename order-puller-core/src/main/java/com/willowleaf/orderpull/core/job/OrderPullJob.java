package com.willowleaf.orderpull.core.job;

import com.willowleaf.orderpull.core.OrderPuller;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 订单拉取的定时任务。
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderPullJob extends QuartzJobBean {

    private OrderPuller puller;

    OrderPullJob(OrderPuller puller) {
        this.puller = puller;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        puller.pullAndProcess(System.out::println);
    }
}
