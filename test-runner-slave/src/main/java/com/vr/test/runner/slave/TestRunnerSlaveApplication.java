package com.vr.test.runner.slave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestRunnerSlaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestRunnerSlaveApplication.class, args);
	}

}
