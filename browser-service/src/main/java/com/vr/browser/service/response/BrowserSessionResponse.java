package com.vr.browser.service.response;

import com.vr.browser.service.request.BrowserType;

public record BrowserSessionResponse(
        String sessionId,
        BrowserType browserType,
        String wsUrl,
        String address,
        int port
) {
}
