package com.vr.browser.service.service;

import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.response.BrowserSessionResponse;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class BrowserService {


    protected String replaceHostAndPort(
            String originalUrl,
            String proxyHost,      // central proxy host
            String proxyPort,      // central proxy port
            String sidecarHost,    // sidecar pod IP
            String sidecarPort     // sidecar nginx port
    ) throws URISyntaxException {

        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new IllegalArgumentException("Original URL cannot be null or empty.");
        }

        URI originalUri = new URI(originalUrl);

        // The Chrome CDP port
        int chromePort = originalUri.getPort();

        URI updatedUri = new URI(
                originalUri.getScheme(),       // ws / wss
                originalUri.getUserInfo(),
                proxyHost,
                Integer.parseInt(proxyPort),
                "/ws/",
                // Query string with sidecar host/port and chromePort
                "host=" + sidecarHost +
                        "&port=" + sidecarPort +
                        "&browserPort=" + chromePort +
                        "&path=" + originalUri.getPath().replaceFirst("^/", ""),
                originalUri.getFragment()
        );

        return updatedUri.toString();
    }


    public abstract BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception;

}
