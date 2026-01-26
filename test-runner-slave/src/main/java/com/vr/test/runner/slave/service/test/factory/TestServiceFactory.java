package com.vr.test.runner.slave.service.test.factory;

import com.vr.test.runner.slave.browser.request.BrowserType;
import com.vr.test.runner.slave.service.test.TestService;
import com.vr.test.runner.slave.service.test.impl.ChromeTestService;
import com.vr.test.runner.slave.service.test.impl.EdgeTestService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TestServiceFactory {
    private final ChromeTestService chromeTestService;
    private final EdgeTestService edgeTestService;

    public TestServiceFactory(ChromeTestService chromeTestService, EdgeTestService edgeTestService) {
        this.chromeTestService = chromeTestService;
        this.edgeTestService = edgeTestService;
    }

    public @NonNull TestService getTestService(@NonNull BrowserType browser) {
        return switch (browser) {
            case CHROME -> chromeTestService;
            case EDGE -> edgeTestService;
        };

    }
}
