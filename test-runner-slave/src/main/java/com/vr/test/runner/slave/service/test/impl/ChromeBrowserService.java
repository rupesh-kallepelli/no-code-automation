package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.v1.chrome.ChromeBrowser;
import com.vr.cdp.actions.v1.browser.Browser;
import com.vr.test.runner.slave.exceptions.BrowserConnectionException;
import com.vr.test.runner.slave.exceptions.ClientSideException;
import com.vr.test.runner.slave.exceptions.NoSuchTestCaseException;
import com.vr.test.runner.slave.exceptions.ServerSideException;
import com.vr.test.runner.slave.request.BrowserRequest;
import com.vr.test.runner.slave.request.enums.BrowserType;
import com.vr.test.runner.slave.response.BrowserSessionResponse;
import com.vr.test.runner.slave.response.SessionDeleteResponse;
import com.vr.test.runner.slave.util.ScreencastBroadcaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
@Scope("prototype")
public class ChromeBrowserService extends ChromiumBrowserService {

    private final WebClient browserClient;
    private final ScreencastBroadcaster screencastBroadcaster;

    public ChromeBrowserService(
            @Qualifier("browserClient") WebClient browserClient,
            ScreencastBroadcaster screencastBroadcaster
    ) {
        this.browserClient = browserClient;
        this.screencastBroadcaster = screencastBroadcaster;
    }

    @Override
    public Mono<Browser> launch(String testCaseId) {
        if (Objects.isNull(testCaseId))
            return Mono.error(new NoSuchTestCaseException("Test case id can't be null"));
        return browserClient.post().uri("/sessions")
                .bodyValue(new BrowserRequest(testCaseId, BrowserType.CHROME))
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
                    String websocketUrl = browserSessionResponse.wsUrl();
                    try {
                        return new ChromeBrowser(
                                testCaseId,
                                websocketUrl,
                                true,
                                screencastBroadcaster
                        );
                    } catch (Exception e) {
                        throw new BrowserConnectionException("Exception while connecting to browser service", e);
                    }
                });
    }

    @Override
    public Mono<SessionDeleteResponse> close(String id) {
        return browserClient.delete()
                .uri("/sessions/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(() -> new ClientSideException("Client side error while closing session : " + clientResponse))
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(() -> new ServerSideException("Server side error while closing session : " + clientResponse))
                )
                .bodyToMono(SessionDeleteResponse.class)
                .doOnSuccess(v -> log.info("Closed connection : {}", id))
                .doOnError(throwable -> log.error("Error while closing connection : {}", id));
    }

}
