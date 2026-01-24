package com.vr.browser.service.registry.service;

import com.vr.browser.service.registry.request.RegisterRequest;
import com.vr.browser.service.registry.response.RegistryResponse;

public interface BrowserRegistryService {
    RegistryResponse register(RegisterRequest registerRequest);
}
