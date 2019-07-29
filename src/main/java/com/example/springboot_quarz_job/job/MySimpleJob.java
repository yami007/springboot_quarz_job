package com.example.springboot_quarz_job.job;

import com.example.springboot_quarz_job.service.TestService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class MySimpleJob implements Job{
    @Autowired
    private TestService testService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        testService.doJob("任务叁叁叁");
    }
}
