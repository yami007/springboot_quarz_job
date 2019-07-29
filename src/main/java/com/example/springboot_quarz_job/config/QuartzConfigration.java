package com.example.springboot_quarz_job.config;

import com.example.springboot_quarz_job.job.MySimpleJob;
import com.example.springboot_quarz_job.job.ScheduleTask;
import com.example.springboot_quarz_job.job.ScheduleTask2;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@PropertySource(value = "quartz.properties")
public class QuartzConfigration {

    @Value("${myTrigger_cron}")
    private String myTriggerCron;

    @Value("${cronJobTrigger_cron}")
    private String cronJobTriggerCron;

    @Value("${cronJobTrigger2_cron}")
    private String cronJobTrigger2Cron;

    /**
     * attention:
     * Details：配置定时任务1
     */
    @Bean(name = "scheduleJobBean")
    public MethodInvokingJobDetailFactoryBean scheduleJobBean(ScheduleTask scheduleTask) {// ScheduleTask为需要执行的任务
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
          /*是否并发执行
          例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
          如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
         */
        jobDetail.setConcurrent(false);
        jobDetail.setName("srd-chhliu");// 设置任务的名字
        jobDetail.setGroup("srd");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
        jobDetail.setTargetObject(scheduleTask); //被执行的对象
        jobDetail.setTargetMethod("sayHello"); //被执行的方法
        return jobDetail;
    }

    /**
     * attention:
     * Details：配置定时任务1的触发器，也就是什么时候触发执行定时任务
     */
    @Bean(name = "cronJobTrigger")
    public CronTriggerFactoryBean cronJobTrigger(@Qualifier("scheduleJobBean") MethodInvokingJobDetailFactoryBean scheduleJobBean) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(scheduleJobBean.getObject());
        tigger.setCronExpression(cronJobTriggerCron);// 什么是否触发，Spring Scheduler Cron表达式
        tigger.setName("srd-chhliu");// trigger的name
        return tigger;
    }

    /**
     * attention:
     * Details：配置定时任务2
     */
    @Bean(name = "scheduleJobBean2")
    public MethodInvokingJobDetailFactoryBean scheduleJobBean2(ScheduleTask2 scheduleTask2) {// ScheduleTask为需要执行的任务
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
          /*是否并发执行
          例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
          如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
         */
        jobDetail.setConcurrent(false);
        jobDetail.setName("srd-chhliu2");// 设置任务的名字
        jobDetail.setGroup("srd");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
        jobDetail.setTargetObject(scheduleTask2); //被执行的对象
        jobDetail.setTargetMethod("sayHello"); //被执行的方法
        return jobDetail;
    }

    /**
     * attention:
     * Details：配置定时任务2的触发器，也就是什么时候触发执行定时任务
     */
    @Bean(name = "cronJobTrigger2")
    public CronTriggerFactoryBean cronJobTrigger2(@Qualifier("scheduleJobBean2") MethodInvokingJobDetailFactoryBean scheduleJobBean2) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(scheduleJobBean2.getObject());
        tigger.setCronExpression(cronJobTrigger2Cron);// 什么是否触发，Spring Scheduler Cron表达式
        tigger.setName("srd-chhliu2");// trigger的name
        return tigger;
    }

    // =======使用implements Job的方式配置定时任务3
    @Bean("myJobDetail")
    public JobDetailFactoryBean myJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(MySimpleJob.class);
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setGroup("srd3");
        return jobDetailFactoryBean;
    }

    @Bean("myTrigger")
    public CronTriggerFactoryBean myTrigger(@Qualifier("myJobDetail") JobDetailFactoryBean myJobDetail) throws IOException {
        CronTriggerFactoryBean myTrigger = new CronTriggerFactoryBean();
        myTrigger.setGroup("srd3");
        myTrigger.setCronExpression(myTriggerCron);
        myTrigger.setJobDetail(myJobDetail.getObject());
        return myTrigger;
    }

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * attention:
     * Details：定义quartz调度工厂
     */
    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(JobFactory jobFactory, @Qualifier("cronJobTrigger") Trigger cronJobTrigger, @Qualifier("cronJobTrigger2") Trigger cronJobTrigger2, @Qualifier("myTrigger") Trigger myTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 注入自定义的jobFactory，不然implements Job配置的job没法使用spring自动注入的bean
        bean.setJobFactory(jobFactory);
        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器，将触发器1和2注册到调度工厂
        bean.setTriggers(cronJobTrigger, cronJobTrigger2, myTrigger);
        return bean;
    }

}