package com.vr.browser.service.registry.request;

public record HeartBeatRequest(String id, Integer activeSessionCount) {
}
