package com.vr.browser.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HeartBeatRequest(
        @NotBlank(message = "Id can't be empty or blank or null") String id,
        @NotNull(message = "activeSessionCount can't be null") Integer activeSessionCount,
        @NotNull(message = "Registration Details are not valid") RegisterRequest registerRequest
) {
}
