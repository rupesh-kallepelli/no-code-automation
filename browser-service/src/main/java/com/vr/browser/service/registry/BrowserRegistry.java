package com.vr.browser.service.registry;

import com.vr.browser.service.exception.NoSuchSession;
import com.vr.launcher.v1.BrowserDetails;
import com.vr.launcher.v1.BrowserLauncher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class BrowserRegistry {

    private final Map<String, BrowserDetails> browser_cache;

    public BrowserRegistry() {
        browser_cache = new HashMap<>();
    }

    public void killBrowserProcess(String id) {
        BrowserDetails details = browser_cache.get(id);
        if (Objects.nonNull(details)) {
            details.getProcess().destroyForcibly();
            details.getUsrDir().delete();
            BrowserLauncher.cleanWSUrl(details.getWsUrl());
        }
        browser_cache.remove(id);
    }

    public void killAll() {
        browser_cache.forEach((id, details) -> {
            if (Objects.nonNull(details)) {
                details.getProcess().destroyForcibly();
                details.getUsrDir().delete();
                BrowserLauncher.cleanWSUrl(details.getWsUrl());
            }
        });
        browser_cache.clear();
    }

    public Map<String, String> getAllBrowserSessions() {
        Map<String, String> sessionMap = new HashMap<>();
        browser_cache.forEach((aLong, browserDetails) ->
                sessionMap.put(String.valueOf(aLong),
                        browserDetails.getAddress() + ":" + browserDetails.getPort()
                )
        );
        return sessionMap;
    }

    public void addNewBrowserProcess(String id, BrowserDetails browserDetails) {
        browser_cache.put(id, browserDetails);
    }

    public URI getURIOf(String sessionId) {
        BrowserDetails browserDetails = browser_cache.get(sessionId);
        if (Objects.isNull(browserDetails)) throw new NoSuchSession("Session is not present with id : " + sessionId);
        URI wsURI = URI.create(browserDetails.getWsUrl());
        try {
            return new URI(
                    wsURI.getScheme(),
                    wsURI.getUserInfo(),
                    wsURI.getHost(),
                    Integer.parseInt(browserDetails.getPort()),
                    wsURI.getPath(),
                    wsURI.getQuery(),
                    wsURI.getFragment()
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }
}
