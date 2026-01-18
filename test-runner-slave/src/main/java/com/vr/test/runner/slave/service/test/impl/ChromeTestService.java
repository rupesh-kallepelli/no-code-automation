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
//            BrowserSessionResponse browserSessionResponse = Objects.requireNonNull(browserClient.post()
//                            .uri("/api/v1/sessions")
//                            .bodyValue(new BrowserRequest(BrowserType.CHROME))
//                            .retrieve()
//                            .toEntity(BrowserSessionResponse.class)
//                            .block())
//                    .getBody();
//            String websocketUrl = browserSessionResponse.wsUrl();
//            return new ChromiumPage(
//                    Long.valueOf(browserSessionResponse.sessionId()),
//                    websocketUrl,
//                    false,
//                    null
//            );
            String websocketUrl = "wss://cdp-proxy-svc-kallepelli-rupesh-dev.apps.rm1.0a51.p1.openshiftapps.com/devtools/page/2A7864643179B83370CBB7336DBA71EA";
            return new ChromiumPage(
                    000L,
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
                .uri("/api/v1/sessions/" + id)
                .retrieve()
                .toEntity(Void.class)
                .block();
    }


}
