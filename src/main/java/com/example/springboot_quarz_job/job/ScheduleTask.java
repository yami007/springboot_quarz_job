package com.example.springboot_quarz_job.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 此注解必加
public class ScheduleTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

	public void sayHello() {
		System.out.println("任务壹");
		LOGGER.info("Hello world, i'm the king of the world!!!");
	}
}