package com.vr.browser.service.registry.request;

import jakarta.validation.constraints.NotNull;

public record BrowserRequest(
        @NotNull(message = "Browser type can't be null") BrowserType browserType
) {
}
