package com.willowleaf.orderpull.core.job;

import com.willowleaf.orderpull.core.OrderPuller;
import com.willowleaf.orderpull.core.TimeInterval;
import com.willowleaf.orderpull.core.data.OperationRepository;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 拉取订单任务的自动化配置。
 */
@EnableTransactionManagement
@Configuration
@EnableConfigurationProperties(JobProperties.class)
public class JobAutoConfiguration {

    private final JobProperties jobProperties;
    private final OrderPuller puller;

    public JobAutoConfiguration(JobProperties jobProperties,
                                OrderPuller puller) {
        this.jobProperties = jobProperties;
        this.puller = puller;
    }

    @Bean
    @ConditionalOnMissingBean(value = Job.class, name = "orderPullJob")
    public QuartzJobBean orderPullJob() {
        return new OrderPullJob(puller);
    }

    @Bean
    public JobDetail jobDetail(QuartzJobBean quartzJob) {
        return JobBuilder.newJob(quartzJob.getClass())
                .withIdentity(jobProperties.getJobIdentity())
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobProperties.getTriggerIdentity())
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(jobProperties.getTimeInterval())
                                .repeatForever()
                )
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(TimeInterval.class)
    public TimeInterval timeInterval(OperationRepository operationRepository, JobProperties jobProperties) {
        return new JdbcTimeInterval(operationRepository, jobProperties);
    }
}
