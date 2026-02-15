package com.vr.ai.test.planner.model.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestCaseStep(
        @NotBlank(message = "Step name can't be null, empty or blank") String name,
        @NotNull(message = "ActionType can't be null") ActionType action,
        boolean isWaitRequired,
        Long timeoutMs,
        String value
) {
}
