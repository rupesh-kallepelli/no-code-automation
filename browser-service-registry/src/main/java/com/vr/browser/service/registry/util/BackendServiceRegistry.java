package com.vr.browser.service.registry.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BackendServiceRegistry {

    private final Map<String, String> podMap = Map.of(
            "pod-1", "wss://browser-service-cdp-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/proxy/44983/devtools/page/CB208CBAA956CEED6790A486F3A990A4",
            "pod-2", "ws://localhost:9002/ws/browser"
    );

    public String getPodUrl(String podId) {
        return podMap.get(podId); // in real case, dynamic discovery via Redis / k8s API
    }
}
