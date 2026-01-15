package com.vr.test.runner.slave.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.test.runner.slave.request.enums.SelectorType;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Selector(
        @NotBlank(message = "selector can't be blank or empty or null") SelectorType selector,
        @NotBlank(message = "value can't be blank or empty or null") String value
) {
}
