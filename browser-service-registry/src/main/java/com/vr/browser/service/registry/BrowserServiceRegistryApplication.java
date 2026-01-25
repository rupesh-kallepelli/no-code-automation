package com.vr.browser.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrowserServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrowserServiceRegistryApplication.class, args);
    }

}
