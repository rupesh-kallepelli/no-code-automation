package com.vr.browser.service.registry.request;

public record BrowserSessionResponse(
        String sessionId,
        BrowserType browserType,
        String wsUrl,
        String address,
        String port
) {
}