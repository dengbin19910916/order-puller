package com.willowleaf.orderpull.core;

import com.willowleaf.orderpull.core.data.OperationRepository;
import org.quartz.*;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 拉取订单任务的自动化配置。
 *
 * @author dengb
 */
@EnableTransactionManagement
@Configuration
@EnableConfigurationProperties(JobProperties.class)
public class JobAutoConfiguration {

    private final JobProperties properties;
    private final OrderPuller puller;
    private final OrderPusher pusher;

    public JobAutoConfiguration(JobProperties properties,
                                OrderPuller puller,
                                OrderPusher pusher) {
        this.properties = properties;
        this.puller = puller;
        this.pusher = pusher;
    }

    @Bean
    @ConditionalOnMissingBean(QuartzJobBean.class)
    public QuartzJobBean quartzJob() {
        return new OrderPullJob(puller, pusher);
    }

    @Bean
    public JobDetail jobDetail(QuartzJobBean quartzJob) {
        return JobBuilder.newJob(quartzJob.getClass())
                .withIdentity(properties.getJobIdentity())
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(properties.getTimeInterval())
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(properties.getJobIdentity())
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(TimeInterval.class)
    public TimeInterval timer(OperationRepository operationRepository, JobProperties jobProperties) {
        return new JdbcIntervalTimeInterval(operationRepository, jobProperties);
    }

    @Bean
    public Queue queue() {
        return new Queue(ORDER_QUEUE_NAME, true);
    }

    static final String ORDER_QUEUE_NAME = "order";
}
