package com.vr.browser.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank(message = "Ip address can't be blank or empty or null") @Pattern(regexp = "^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$") String ipAddress,
        @NotNull(message = "Port can't be null") Integer port,
        @NotNull(message = "Browser can't be null") BrowserType browserType) {
}
