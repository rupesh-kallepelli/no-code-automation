package com.vr.test.runner.slave.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final String browserServiceHost;

    public WebClientConfig(
            @Value("${browser.service}") String browserService
    ) {
        this.browserServiceHost = browserService;
    }

    @Bean
    public WebClient browserClient() {
        return WebClient.builder()
                .baseUrl(browserServiceHost)
                .build();
    }
}
