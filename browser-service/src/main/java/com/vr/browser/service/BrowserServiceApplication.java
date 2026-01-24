package com.vr.browser.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrowserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrowserServiceApplication.class, args);
	}

}
