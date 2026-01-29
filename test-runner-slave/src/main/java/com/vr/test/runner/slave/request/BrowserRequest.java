package com.vr.test.runner.slave.request;

import com.vr.test.runner.slave.request.enums.BrowserType;
import jakarta.validation.constraints.NotNull;

public record BrowserRequest(
        @NotNull(message = "Browser type can't be null") BrowserType browserType
) {
}
