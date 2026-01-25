package com.vr.browser.service.registry.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank String ipAddress,
        @NotNull Integer port,
        @NotNull BrowserType browserType) {
}
