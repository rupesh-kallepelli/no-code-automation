package com.vr.browser.service.request;

public record HeartBeatRequest(String id, Integer activeSessionCount) {
}
