package com.vr.browser.service.scheduler;

import com.vr.browser.service.exception.ClientException;
import com.vr.browser.service.exception.ServerException;
import com.vr.browser.service.registry.BrowserRegistry;
import com.vr.browser.service.request.BrowserType;
import com.vr.browser.service.request.HeartBeatRequest;
import com.vr.browser.service.request.RegisterRequest;
import com.vr.browser.service.response.HeartBeatResponse;
import com.vr.browser.service.response.RegistryResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BrowserServiceRegisterScheduler {
    private final WebClient registryClient;
    private final String ipAddress;
    private final Integer port;
    private String id;
    private final BrowserRegistry browserRegistry;

    public BrowserServiceRegisterScheduler(
            @Qualifier("registryClient") WebClient registryClient,
            @Value("${ip.address:127.0.0.1}") String ipAddress,
            @Value("${server.port}") Integer port,
            BrowserRegistry browserRegistry) {
        this.registryClient = registryClient;
        this.ipAddress = ipAddress;
        this.port = port;
        this.browserRegistry = browserRegistry;
    }

//    @Scheduled(fixedRate = 3000)
    public void register() {
        registryClient.
                post()
                .uri("/heart-beat")
                .bodyValue(new HeartBeatRequest(id, browserRegistry.activeSessionCount()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new ClientException("Client error while sending hear beat to registry" + clientResponse)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new ServerException("Server error while sending hear beat to registry" + clientResponse)))
                .bodyToMono(HeartBeatResponse.class)
                .doOnSuccess(heartBeatResponse -> log.info("Heart beat sent {}", heartBeatResponse))
                .doOnError(throwable -> log.error("Error while sending heart beat", throwable))
                .block();
    }

//    @PostConstruct
    public void init() {
        RegistryResponse registryResponse = registryClient.
                post()
                .uri("/register")
                .bodyValue(new RegisterRequest(ipAddress, port, BrowserType.CHROME))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new ClientException("Client error while registering with registry" + clientResponse)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new ServerException("Server error while registering with registry" + clientResponse)))
                .bodyToMono(RegistryResponse.class)
                .doOnSuccess(response -> {
                    log.info("Service will register with id : {}, meessage: {}", id, response.message());
                    id = response.id();
                })
                .doOnError(throwable -> log.error("Unable to get the registration id"))
                .block();
        assert registryResponse != null;
        this.id = registryResponse.id();
    }
}
