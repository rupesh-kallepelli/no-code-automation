package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.page.v1.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class EdgeTestService extends ChromiumTestService {

    @Override
    public Page launch() {
        throw new RuntimeException("Edge is not supported currently");
    }

    @Override
    public void close(Long id) {
        throw new RuntimeException("Edge is not supported currently");
    }
}
