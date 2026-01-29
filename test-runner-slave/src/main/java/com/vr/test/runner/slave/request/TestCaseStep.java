package com.vr.test.runner.slave.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.test.runner.slave.request.enums.ActionType;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestCaseStep(
        @NotNull(message = "ActionType can't be null") ActionType action,
        Long timeoutMs,
        Selector selector,
        String value

) {
}
