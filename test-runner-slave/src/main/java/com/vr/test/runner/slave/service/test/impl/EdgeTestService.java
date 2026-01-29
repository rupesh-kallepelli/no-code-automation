package com.vr.test.runner.slave.service.test.impl;

import com.vr.cdp.actions.v1.page.Page;
import com.vr.test.runner.slave.response.SessionDeleteResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import reactor.core.publisher.Mono;

@Service
@RequestScope
public class EdgeTestService extends ChromiumPageService {

    @Override
    public Mono<Page> launch() {
        throw new RuntimeException("Edge is not supported currently");
    }

    @Override
    public Mono<SessionDeleteResponse> close(String id) {
        throw new RuntimeException("Edge is not supported currently");
    }
}
