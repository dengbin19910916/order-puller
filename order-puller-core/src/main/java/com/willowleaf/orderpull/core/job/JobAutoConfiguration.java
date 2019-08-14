package com.willowleaf.orderpull.core.job;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.OrderPusher;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.data.OperationRepository;
import org.quartz.*;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.UUID;

/**
 * 拉取订单任务的自动化配置。
 */
@EnableTransactionManagement
@Configuration
@EnableConfigurationProperties(JobProperties.class)
public class JobAutoConfiguration {

    private final JobProperties jobProperties;
    private final OrderPuller puller;
    private final OrderPusher pusher;

    public JobAutoConfiguration(JobProperties jobProperties,
                                OrderPuller puller,
                                OrderPusher pusher) {
        this.jobProperties = jobProperties;
        this.puller = puller;
        this.pusher = pusher;
    }

    @Bean
    @ConditionalOnMissingBean(value = Job.class, name = "orderPullJob")
    public QuartzJobBean orderPullJob() {
        return new OrderPullJob(puller, pusher);
    }

    @Bean
    public JobDetail jobDetail(QuartzJobBean quartzJob, TimeInterval timeInterval) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("strategy", new OrderPuller.Strategy(timeInterval, 0, jobProperties.getSize()));
        return JobBuilder.newJob(quartzJob.getClass())
                .withIdentity(getIdentity())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(jobProperties.getTimeInterval())
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(getIdentity())
                .withSchedule(scheduleBuilder)
                .build();
    }

    private String getIdentity() {
        return jobProperties.getJobIdentity() == null
                ? UUID.randomUUID().toString().replaceAll("-", "")
                : jobProperties.getJobIdentity();
    }

    @Bean
    @ConditionalOnMissingBean(TimeInterval.class)
    public TimeInterval timeInterval(OperationRepository operationRepository, JobProperties jobProperties) {
        return new JdbcTimeInterval(operationRepository, jobProperties);
    }

    @Bean
    public Queue queue() {
        return new Queue(ORDER_QUEUE_NAME, true);
    }

    /**
     * 消息Queue的名字
     */
    public static final String ORDER_QUEUE_NAME = "order";
}
