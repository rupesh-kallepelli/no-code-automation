package com.vr.test.runner.slave.browser.response;


import com.vr.test.runner.slave.browser.request.BrowserType;

public record BrowserSessionResponse(
        String sessionId,
        BrowserType browserType,
        String wsUrl,
        String address,
        int port
) {
}
