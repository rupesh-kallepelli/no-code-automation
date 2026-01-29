package com.vr.test.runner.slave.response;


import com.vr.test.runner.slave.request.enums.BrowserType;

public record BrowserSessionResponse(
        String sessionId,
        BrowserType browserType,
        String wsUrl,
        String address,
        int port
) {
}
