package com.vr.test.runner.slave.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.test.runner.slave.browser.request.BrowserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestCase(
        @NotBlank(message = "testName can't be blank or empty or null") String testName,
        @NotNull(message = "Browser type can't be null") BrowserType browser,
        @NotNull(message = "TestCaseStep can't be null") List<TestCaseStep> steps
) {
}
