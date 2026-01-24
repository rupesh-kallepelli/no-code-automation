package com.vr.browser.service.request;

public record RegisterRequest(
        String ipAddress,
        Integer port) {
}
