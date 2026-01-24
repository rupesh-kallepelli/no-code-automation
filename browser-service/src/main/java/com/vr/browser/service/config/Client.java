package com.vr.browser.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Client {
    private final String registryHost;

    public Client(@Value("${registry.host}") String registryHost) {
        this.registryHost = registryHost;
    }

    @Bean
    public WebClient registryClient() {
        return WebClient.builder()
                .baseUrl(registryHost)
                .build();
    }

}
