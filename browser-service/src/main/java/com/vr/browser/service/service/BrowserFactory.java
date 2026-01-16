package com.vr.browser.service.service;

import com.vr.browser.service.exception.NoBrowserSupportYetException;
import com.vr.browser.service.request.BrowserType;
import com.vr.browser.service.service.impl.ChromeLauncherService;
import org.springframework.stereotype.Component;

@Component
public class BrowserFactory {
    private final ChromeLauncherService chromeLauncherService;

    public BrowserFactory(ChromeLauncherService chromeLauncherService) {
        this.chromeLauncherService = chromeLauncherService;
    }

    public BrowserService getBrowser(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> chromeLauncherService;
            case EDGE -> throw new NoBrowserSupportYetException("Edge browser is not supported yet");
        };
    }
}
