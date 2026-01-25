package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.v1.page.Page;
import com.vr.actions.v1.page.chromium.ChromePage;
import com.vr.test.runner.slave.browser.request.BrowserRequest;
import com.vr.test.runner.slave.browser.request.BrowserType;
import com.vr.test.runner.slave.browser.response.BrowserSessionResponse;
import com.vr.test.runner.slave.exceptions.ClientSideException;
import com.vr.test.runner.slave.exceptions.ServerSideException;
import com.vr.test.runner.slave.util.ScreencastBroadcaster;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Scope("prototype")
public class ChromeTestService extends ChromiumTestService {

    private final WebClient browserClient;
    private final ScreencastBroadcaster screencastBroadcaster;

    public ChromeTestService(
            @Qualifier("browserClient") WebClient browserClient,
            ScreencastBroadcaster screencastBroadcaster
    ) {
        this.browserClient = browserClient;
        this.screencastBroadcaster = screencastBroadcaster;
    }

    @Override
    public Mono<Page> launch() {
        return browserClient.post().uri("/sessions")
                .bodyValue(new BrowserRequest(BrowserType.CHROME))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(() -> new ClientSideException("Client side error while creating session : " + clientResponse))
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(() -> new ServerSideException("Server side error while creating session : " + clientResponse))
                )
                .bodyToMono(BrowserSessionResponse.class)
                .map(browserSessionResponse -> {
                    assert browserSessionResponse != null;
                    String websocketUrl = browserSessionResponse.wsUrl().replace("ws://", "wss://");
                    try {
                        return new ChromePage(
                                browserSessionResponse.sessionId(),
                                websocketUrl,
                                true,
                                screencastBroadcaster
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void close(String id) {
        ResponseEntity<Void> block = browserClient.delete()
                .uri("/api/v1/sessions/" + id)
                .retrieve()
                .toEntity(Void.class)
                .block();
    }


}
