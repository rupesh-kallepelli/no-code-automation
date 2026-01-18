package com.vr.browser.service.service;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.launcher.v1.BrowserDetails;
import com.vr.launcher.v1.BrowserLauncher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface BrowserService {
    Map<String, BrowserDetails> PROCESS_CACHE = new HashMap<>();

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
        PROCESS_CACHE.clear();
    }

    static void addNewBrowserProcess(String id, BrowserDetails browserDetails) {
        PROCESS_CACHE.put(id, browserDetails);
    }

    default String replaceHostAndPort(String originalUrl, String newHost, int newPort)
            throws URISyntaxException {

        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new IllegalArgumentException("Original URL cannot be null or empty.");
        }
        if (newHost == null || newHost.isEmpty()) {
            throw new IllegalArgumentException("New host cannot be null or empty.");
        }

        URI originalUri = new URI(originalUrl);

        URI updatedUri = new URI(
                originalUri.getScheme(),      // ws / wss / http / https
                originalUri.getUserInfo(),
                newHost,
                -1, //change to newPort in local
                "/proxy/" + newPort + originalUri.getPath(),
                originalUri.getQuery(),
                originalUri.getFragment()
        );

        return updatedUri.toString();
    }
    BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception;

}
