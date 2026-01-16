package com.vr.browser.service.request;

import jakarta.validation.constraints.NotNull;

public record BrowserRequest(
        @NotNull(message = "Browser type can't be null") BrowserType browserType
) {
}
