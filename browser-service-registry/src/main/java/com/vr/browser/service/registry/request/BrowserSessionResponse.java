package com.vr.browser.service.registry.request;

import lombok.Data;

@Data
public class BrowserSessionResponse {
    private String sessionId;
    private BrowserType browserType;
    private String wsUrl;
    private String address;
    private String port;
}