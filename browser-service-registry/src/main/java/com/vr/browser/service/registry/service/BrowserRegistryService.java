package com.vr.browser.service.registry.service;

import com.vr.browser.service.registry.request.BrowserRequest;
import com.vr.browser.service.registry.request.BrowserSessionResponse;
import com.vr.browser.service.registry.request.HeartBeatRequest;
import com.vr.browser.service.registry.request.RegisterRequest;
import com.vr.browser.service.registry.response.HeartBeatResponse;
import com.vr.browser.service.registry.response.RegistryResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface BrowserRegistryService {
    RegistryResponse register(RegisterRequest registerRequest);

    HeartBeatResponse heartBeat(HeartBeatRequest heartBeatRequest);

    Set<String> getRegisteredServices();

    Mono<BrowserSessionResponse> requestBrowserSession(BrowserRequest browserRequest);
}
