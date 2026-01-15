package com.vr.test.runner.slave.service.test.impl;

import com.vr.launcher.BrowserInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class EdgeTestService extends ChromiumTestService {

    @Override
    public BrowserInstance launch() {
        throw new RuntimeException("Edge is not supported currently");
    }
}
