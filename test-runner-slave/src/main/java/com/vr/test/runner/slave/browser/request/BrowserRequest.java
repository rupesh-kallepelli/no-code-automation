package com.vr.test.runner.slave.browser.request;

import jakarta.validation.constraints.NotNull;

public record BrowserRequest(
        @NotNull(message = "Browser type can't be null") BrowserType browserType
) {
}
