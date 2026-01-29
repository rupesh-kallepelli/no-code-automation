package com.vr.actions.v1.page.chromium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.actions.v1.client.PageCDPClient;
import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.ElementResolver;
import com.vr.actions.v1.page.chromium.exception.*;
import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.cdp.client.ws.ScreenCastClient;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.dom.DOMGetOuterHTML;
import com.vr.cdp.protocol.command.page.*;
import com.vr.cdp.protocol.command.runtime.RuntimeEnable;

import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ChromePage implements Page {

    private final String id;
    private final CDPClient client;
    private final ElementResolver elementResolver;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChromePage(String id, String pageWs) {
        this.id = id;
        try {
            this.client = new PageCDPClient(pageWs, this);
            this.elementResolver = new ElementResolver(client);
        } catch (Exception e) {
            throw new PageCreationException("Exception while creating page", e);
        }
    }

    public ChromePage(
            String id,
            String pageWs,
            boolean enableBroadCasting,
            BroadCaster broadCaster
    ) {
        try {
            this.id = id;
            if (enableBroadCasting) {
                if (Objects.isNull(broadCaster)) throw new BroadCasterCannotBeNull("Broadcaster is null");
                this.client = new ScreenCastClient(pageWs, broadCaster);
            } else
                this.client = new PageCDPClient(pageWs, this);
            this.elementResolver = new ElementResolver(client);
        } catch (Exception e) {
            throw new PageCreationException("Exception while creating page", e);
        }
    }

    private volatile CompletableFuture<Void> navigationFuture;
    private String mainFrameId;

    @Override
    public void onEvent(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            String method = node.has("method") ? node.get("method").asText() : "";

            switch (method) {
                case "Page.frameStartedNavigating" -> {
                    String frameId = node.get("params").get("frameId").asText();
                    if (!frameId.equals(mainFrameId)) return;
                    navigationFuture = new CompletableFuture<>();
                }
                case "Page.frameStoppedLoading", "Page.frameNavigated" -> {
                    String frameId = node.get("params").has("frameId")
                            ? node.get("params").get("frameId").asText()
                            : null;
                    if (frameId != null && frameId.equals(mainFrameId) && navigationFuture != null) {
                        navigationFuture.complete(null);
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // wait for navigation to finish
    public void waitForNavigation() {
        if (navigationFuture != null) {
            navigationFuture.join(); // blocks only the caller, not the event thread
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String navigate(String url) {
        try {
            client.send(new PageEnable());
            client.send(new RuntimeEnable());
            client.send(new PageSetLifecycleEventsEnabled(true));
            PageNavigate.Result pageNavigationResult = client.sendAndWait(new PageNavigate(url));
            mainFrameId = pageNavigationResult.frameId();
            waitForNavigation();
            return mainFrameId;
        } catch (Exception e) {
            throw new NavigationException("Unable to navigate due to :", e);
        }
    }

    @Override
    public void reload() {
        try {
            client.sendAndWait(new PageReload(true));
        } catch (Exception e) {
            throw new ReloadException("Exception while reload", e);
        }
    }

    @Override
    public byte[] screenshot() {
        try {
            PageCaptureScreenshot.Result result = client.sendAndWait(new PageCaptureScreenshot());
            return Base64.getDecoder().decode(result.data());
        } catch (Exception e) {
            throw new ScreenshotException("Exception while taking screenshot", e);
        }
    }

    @Override
    public byte[] screenshotFullPage() {
        try {
            PageCaptureScreenshot.Result result = client.sendAndWait(new PageCaptureScreenshot(true));
            return Base64.getDecoder().decode(result.data());
        } catch (Exception e) {
            throw new ScreenshotException("Exception while taking screenshot", e);
        }
    }

    @Override
    public void cast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    ) {
        try {
            client.send(new PageStartScreencast(format, quality, maxWidth, maxHeight));
        } catch (Exception e) {
            throw new NavigationException("Unable to cast due to :", e);
        }
    }

    @Override
    public String getPageSource() {
        try {
            return client.sendAndWait(new DOMGetOuterHTML(
                    client.sendAndWait(new DOMGetDocument()).root().nodeId()
            )).outerHTML();
        } catch (Exception e) {
            throw new PageSourceException("Exception while getting page source", e);
        }
    }

    @Override
    public Element findElement(Selector selector) {
        return elementResolver.resolveElementWithTimeout(selector, 5000);
    }

    @Override
    public Element findElementWithTimeout(Selector selector, long millis) {
        return elementResolver.resolveElementWithTimeout(selector, millis);
    }

    @Override
    public void close() {
        try {
            client.send(new PageDisable());
            client.close();
        } catch (Exception e) {
            throw new PageCloseException("Error occurred while closing page", e);
        }
    }

}
