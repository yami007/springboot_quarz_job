package com.example.springboot_quarz_job.job;

import com.example.springboot_quarz_job.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // 此注解必加
public class ScheduleTask2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask2.class);
    @Autowired
    private TestService testService;

    public void sayHello() {
        testService.doJob("任务贰贰");
        LOGGER.info("Hello world, i'm the king of the world!!!");
    }
}