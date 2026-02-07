package com.vr.test.runner.slave.request;

import com.vr.test.runner.slave.request.enums.BrowserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BrowserRequest(
        @NotBlank(message = "Test case id can't be null, empty or blank") String testCaseId,
        @NotNull(message = "Browser type can't be null") BrowserType browserType
) {
}
