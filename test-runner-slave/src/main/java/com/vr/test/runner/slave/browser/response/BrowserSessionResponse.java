package com.vr.test.runner.slave.browser.response;


import com.vr.test.runner.slave.browser.request.BrowserType;

public record BrowserSessionResponse(
        BrowserType browserType,
        String websocketUrl,
        Long id
) {
}
