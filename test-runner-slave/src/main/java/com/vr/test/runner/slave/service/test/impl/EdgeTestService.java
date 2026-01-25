package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.v1.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import reactor.core.publisher.Mono;

@Service
@RequestScope
public class EdgeTestService extends ChromiumTestService {

    @Override
    public Mono<Page> launch() {
        throw new RuntimeException("Edge is not supported currently");
    }

    @Override
    public void close(String id) {
        throw new RuntimeException("Edge is not supported currently");
    }
}
