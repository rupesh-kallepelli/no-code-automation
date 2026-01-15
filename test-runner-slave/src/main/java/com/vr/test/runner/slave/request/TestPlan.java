package com.vr.test.runner.slave.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.test.runner.slave.request.enums.Browser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestPlan(
        @NotNull(message = "Browser type can't be null") Browser browser,
        @NotBlank(message = "testName can't be blank or empty or null") String testName,
        @NotNull(message = "Step can't be null") List<Step> steps
) {
}
