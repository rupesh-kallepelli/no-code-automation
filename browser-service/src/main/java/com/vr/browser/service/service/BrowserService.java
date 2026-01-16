package com.vr.browser.service.service;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.launcher.v1.BrowserDetails;
import com.vr.launcher.v1.BrowserLauncher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface BrowserService {
    Map<Long, BrowserDetails> processMap = new HashMap<>();

    static void killBrowserProcess(Long id) {
        BrowserDetails details = processMap.get(id);
        if (Objects.nonNull(details)) {
            details.getProcess().destroyForcibly();
            details.getUsrDir().delete();
            BrowserLauncher.cleanWSUrl(details.getWsUrl());
        }
    }

    static void addNewBrowserProcess(Long id, BrowserDetails browserDetails) {
        processMap.put(id, browserDetails);
    }

    BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception;

}
