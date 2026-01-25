package com.vr.browser.service.service;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class BrowserService {


    protected String replaceHostAndPort(
            String originalUrl,
            String newHost,
            String port,
            String sessionId
    ) throws URISyntaxException {

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
                Integer.parseInt(port), //change to newPort in local
                "/ws",
                "session=" + sessionId,
                originalUri.getFragment()
        );

        return updatedUri.toString();
    }

    public abstract BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception;

}
