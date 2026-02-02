package com.vr.test.runner.slave.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.test.runner.slave.request.enums.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestCaseStep(
        @NotBlank(message = "Step name can't be null, empty or blank") String name,
        @NotNull(message = "ActionType can't be null") ActionType action,
        boolean isWaitRequired,
        Long timeoutMs,
        Selector sourceSelector,
        Selector targetSelector,
        String value
) {
}
