package com.vr.browser.service.service;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.launcher.v1.BrowserDetails;
import com.vr.launcher.v1.BrowserLauncher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface BrowserService {
    Map<Long, BrowserDetails> PROCESS_CACHE = new HashMap<>();

    static void killBrowserProcess(Long id) {
        BrowserDetails details = PROCESS_CACHE.get(id);
        if (Objects.nonNull(details)) {
            details.getProcess().destroyForcibly();
            details.getUsrDir().delete();
            BrowserLauncher.cleanWSUrl(details.getWsUrl());
        }
    }

    static void killAll() {
        PROCESS_CACHE.forEach((id, details) -> {
            if (Objects.nonNull(details)) {
                details.getProcess().destroyForcibly();
                details.getUsrDir().delete();
                BrowserLauncher.cleanWSUrl(details.getWsUrl());
            }
        });
    }

    static void addNewBrowserProcess(Long id, BrowserDetails browserDetails) {
        PROCESS_CACHE.put(id, browserDetails);
    }

    BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception;

}
