package com.vr.test.runner.slave.service.test.factory;

import com.vr.test.runner.slave.request.enums.BrowserType;
import com.vr.test.runner.slave.service.test.PageService;
import com.vr.test.runner.slave.service.test.impl.ChromePageService;
import com.vr.test.runner.slave.service.test.impl.EdgeTestService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TestServiceFactory {
    private final ChromePageService chromeTestService;
    private final EdgeTestService edgeTestService;

    public TestServiceFactory(ChromePageService chromeTestService, EdgeTestService edgeTestService) {
        this.chromeTestService = chromeTestService;
        this.edgeTestService = edgeTestService;
    }

    public @NonNull PageService getTestService(@NonNull BrowserType browser) {
        return switch (browser) {
            case CHROME -> chromeTestService;
            case EDGE -> edgeTestService;
        };

    }
}
