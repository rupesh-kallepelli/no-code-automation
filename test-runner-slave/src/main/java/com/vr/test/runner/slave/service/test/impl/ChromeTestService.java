package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.page.v1.Page;
import com.vr.actions.page.v1.chromium.ChromiumPage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.reactive.function.client.WebClient;

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
//                            .uri("/api/v1/create")
//                            .bodyValue(new BrowserRequest(BrowserType.CHROME))
//                            .retrieve()
//                            .toEntity(BrowserSessionResponse.class)
//                            .block())
//                    .getBody();
//            String websocketUrl = browserSessionResponse.websocketUrl();
//            return new ChromiumPage(
//                    browserSessionResponse.id(),
//                    websocketUrl,
//                    false,
//                    null
//            );
            String websocketUrl = "wss://browser-service-cdp-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/devtools/page/E552713CDFD8910704F2DC015D27E591";
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
                .uri("/api/v1/close?id=" + id)
                .retrieve()
                .toEntity(Void.class)
                .block();
    }


}
