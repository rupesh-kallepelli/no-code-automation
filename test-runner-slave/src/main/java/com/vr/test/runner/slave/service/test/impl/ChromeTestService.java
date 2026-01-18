package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.page.v1.Page;
import com.vr.actions.page.v1.chromium.ChromiumPage;
import com.vr.test.runner.slave.browser.request.BrowserRequest;
import com.vr.test.runner.slave.browser.request.BrowserType;
import com.vr.test.runner.slave.browser.response.BrowserSessionResponse;
import com.vr.test.runner.slave.util.ScreencastBroadcaster;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequestScope
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
    public Page launch() {
        try {
            BrowserSessionResponse browserSessionResponse = Objects.requireNonNull(browserClient.post()
                            .uri("/api/v1/sessions")
                            .bodyValue(new BrowserRequest(BrowserType.CHROME))
                            .retrieve()
                            .toEntity(BrowserSessionResponse.class)
                            .block())
                    .getBody();
            assert browserSessionResponse != null;
            String websocketUrl = browserSessionResponse.wsUrl().replace("ws://", "wss://");
            return new ChromiumPage(
                    browserSessionResponse.sessionId(),
                    websocketUrl,
                    true,
                    screencastBroadcaster
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
