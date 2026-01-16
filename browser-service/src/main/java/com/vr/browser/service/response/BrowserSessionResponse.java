package com.vr.browser.service.response;

import com.vr.browser.service.request.BrowserType;

public record BrowserSessionResponse(
        BrowserType browserType,
        String websocketUrl,
        Long id
) {
}
