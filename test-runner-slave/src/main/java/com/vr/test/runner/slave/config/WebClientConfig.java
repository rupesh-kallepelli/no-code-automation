package com.vr.test.runner.slave.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${browser.service}")
    private String browserService;

    @Bean
    public WebClient browserClient() {
        return WebClient.builder()
                .baseUrl(browserService)
                .build();
    }
}
