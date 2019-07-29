package com.example.springboot_quarz_job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringbootQuarzJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootQuarzJobApplication.class, args);
	}

}
