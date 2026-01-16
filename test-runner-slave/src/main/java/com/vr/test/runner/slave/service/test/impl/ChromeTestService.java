package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.page.v1.Page;
import com.vr.actions.page.v1.chromium.ChromiumPage;
import com.vr.test.runner.slave.browser.request.BrowserRequest;
import com.vr.test.runner.slave.browser.request.BrowserType;
import com.vr.test.runner.slave.browser.response.BrowserSessionResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequestScope
public class ChromeTestService extends ChromiumTestService {

    private WebClient browserClient;

    public ChromeTestService(@Qualifier("browserClient") WebClient browserClient) {
        this.browserClient = browserClient;
    }

    @Override
    public Page launch() {
        try {
            BrowserSessionResponse browserSessionResponse = Objects.requireNonNull(browserClient.post()
                            .uri("/api/v1/create")
                            .bodyValue(new BrowserRequest(BrowserType.CHROME))
                            .retrieve()
                            .toEntity(BrowserSessionResponse.class)
                            .block())
                    .getBody();
            String websocketUrl = browserSessionResponse.websocketUrl();
            return new ChromiumPage(
                    browserSessionResponse.id(),
                    websocketUrl,
                    false,
                    null
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close(Long id) {
        ResponseEntity<Void> block = browserClient.delete()
                .uri("/api/v1/close?id=" + id)
                .retrieve()
                .toEntity(Void.class)
                .block();
    }


}
