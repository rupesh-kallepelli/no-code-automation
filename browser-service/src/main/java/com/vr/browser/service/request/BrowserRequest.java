package com.vr.browser.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BrowserRequest(
        @NotBlank(message = "Test case id can't be null, empty or blank") String testCaseId,
        @NotNull(message = "Browser type can't be null") BrowserType browserType
) {
}
