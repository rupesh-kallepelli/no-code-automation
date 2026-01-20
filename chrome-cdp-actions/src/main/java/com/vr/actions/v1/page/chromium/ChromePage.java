package com.vr.actions.v1.page.chromium;

import com.fasterxml.jackson.databind.JsonNode;
import com.vr.actions.dom.DOMActions;
import com.vr.actions.input.InputActions;
import com.vr.actions.uitl.ScreenshotUtil;
import com.vr.actions.v1.client.PageCDPClient;
import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.ElementResolver;
import com.vr.actions.v1.element.selector.Selector;
import com.vr.actions.v1.page.Page;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.cdp.client.ws.ScreenCastClient;
import com.vr.cdp.protocol.command.page.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChromePage implements Page {
    private final String id;
    private final CDPClient client;
    private final ElementResolver elementResolver;
    private PageState pageState = PageState.READY;
    private final DOMActions dom;
    private final InputActions input;
    private final PageWaiter waiter = new PageWaiter();
    private final Map<Selector, Element> ELEMENT_REGISTRY = new HashMap<>();
    private volatile boolean frameStopped = false;
    private volatile boolean networkIdle = false;
    private volatile boolean interactive = false;

    private volatile long lastDomMutationTs = System.currentTimeMillis();

    private static final long DOM_STABLE_MS = 300;
    private volatile String mainFrameId;


    public ChromePage(
            String id,
            String pageWs
    ) throws Exception {
        this.id = id;
        this.client = new PageCDPClient(pageWs, this);
        this.dom = new DOMActions(client);
        this.input = new InputActions(client);
        this.elementResolver = new ElementResolver(client);
        client.send(new PageEnable());
        client.send(new PageSetLifecycleEventsEnabled(true));
    }

    public ChromePage(
            String id,
            String pageWs,
            boolean enableBroadCasting,
            BroadCaster broadCaster
    ) throws Exception {
        this.id = id;
        if (enableBroadCasting) {
            if (Objects.isNull(broadCaster)) throw new BroadCasterCannotBeNull("Broadcaster is null");
            this.client = new ScreenCastClient(pageWs, broadCaster);
        } else
            this.client = new PageCDPClient(pageWs, this);
        client.send(new PageEnable());
        this.dom = new DOMActions(client);
        this.input = new InputActions(client);
        this.elementResolver = new ElementResolver(client);
    }

    @Override
    public void onEvent(JsonNode json) {
        String method = json.get("method").asText();

        switch (method) {
            /* -------------------------
               Navigation starts
            -------------------------- */
            case "Page.frameStartedNavigating",
                 "Page.frameStartedLoading" -> {
                resetReadiness();
                transitionTo(PageState.BLOCKED);
            }
            /* -------------------------
               New main document
            -------------------------- */
            case "Page.frameNavigated" -> {
                JsonNode frame = json.get("params").get("frame");
                mainFrameId = frame.get("id").asText();
                invalidateElements();
                resetReadiness();
                transitionTo(PageState.BLOCKED);
            }
            /* -------------------------
               Browser finished loading
            -------------------------- */
            case "Page.frameStoppedLoading" -> {
                String frameId = json.get("params").get("frameId").asText();
                if (!frameId.equals(mainFrameId)) return;
                frameStopped = true;
                transitionTo(PageState.BROWSER_LOADED);
                maybeMarkReady();
            }
            /* -------------------------
               Lifecycle events (SPA)
            -------------------------- */
            case "Page.lifecycleEvent" -> {
                String frameId = json.get("params").get("frameId").asText();
                String name = json.get("params").get("name").asText();
                // Ignore non-main-frame signals
                if (!frameId.equals(mainFrameId)) return;
                if ("networkIdle".equals(name)) {
                    networkIdle = true;
                    maybeMarkReady();
                }
                if ("InteractiveTime".equals(name)) {
                    interactive = true;
                    maybeMarkReady();
                }
            }
            /* -------------------------
               DOM mutations (rendering)
            -------------------------- */
            case "DOM.childNodeInserted",
                 "DOM.childNodeRemoved",
                 "DOM.attributeModified" -> {

                lastDomMutationTs = System.currentTimeMillis();

                if (getPageState() == PageState.BROWSER_LOADED
                        || getPageState() == PageState.APP_LOADING) {

                    transitionTo(PageState.APP_LOADING);
                }
            }
            /* -------------------------
               Frame destroyed
            -------------------------- */
            case "Page.frameDetached" -> {
                String frameId = json.get("params").get("frameId").asText();
                String reason = json.get("params").get("reason").asText();
                if ("swap".equals(reason) && frameId.equals(mainFrameId)) {
                    resetReadiness();
                    transitionTo(PageState.BLOCKED);
                }
                if ("remove".equals(reason)) {
                    transitionTo(PageState.DETACHED);
                }
            }
        }
    }


    private void maybeMarkReady() {
        boolean browserReady = frameStopped || interactive;

        if (!browserReady) return;
        if (!networkIdle) return;

        long quietFor = System.currentTimeMillis() - lastDomMutationTs;
        if (quietFor < DOM_STABLE_MS) return;

        transitionTo(PageState.READY);
    }


    private void resetReadiness() {
        frameStopped = false;
        networkIdle = false;
        interactive = false;
        lastDomMutationTs = System.currentTimeMillis();
    }


    private void invalidateElements() {
        ELEMENT_REGISTRY.values().forEach(Element::invalidate);
        ELEMENT_REGISTRY.clear();
    }

    private void transitionTo(PageState newState) {
        if (pageState == newState) return;

        pageState = newState;

        if (newState == PageState.READY || newState == PageState.DETACHED) {
            waiter.release();
        }
    }

    @Override
    public PageState getPageState() {
        return this.pageState;
    }

    @Override
    public void setPageState(PageState pageState) {
        this.pageState = pageState;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void enable() throws Exception {
        client.send(new PageEnable());
    }

    @Override
    public void disable() throws Exception {
        client.send(new PageDisable());
    }

    @Override
    public String navigate(String url) throws Exception {
        PageNavigate.Result r = client.sendAndWait(new PageNavigate(url));
        return r.frameId();
    }

    @Override
    public void reload() throws Exception {
        client.sendAndWait(new PageReload(true));
    }

    @Override
    public byte[] screenshotPng() throws Exception {
        PageCaptureScreenshot.Result r = client.sendAndWait(new PageCaptureScreenshot("png"));
        return Base64.getDecoder().decode(r.data());
    }

    @Override
    public void screenshot(String file) throws Exception {
        var result = client.sendAndWait(new PageCaptureScreenshot("png"));
        ScreenshotUtil.saveBase64Image(result.data(), file);
    }

    @Override
    public void click(String selector) throws Exception {
        int nodeId = dom.find(selector);
        input.click(nodeId);
    }

    @Override
    public void type(String selector, String text) throws Exception {
        int nodeId = dom.find(selector);
        dom.focus(nodeId);
        input.type(text);
    }

    @Override
    public void cast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    ) throws Exception {
        client.send(new PageStartScreencast(format, quality, maxWidth, maxHeight));
    }

    @Override
    public Element findElement(Selector selector) {
        if (ELEMENT_REGISTRY.containsKey(selector)) {
            return ELEMENT_REGISTRY.get(selector);
        }
        Element element = waiter.waitUntilReadyAndThenExecute(this, () -> elementResolver.resolveElement(selector));
        ELEMENT_REGISTRY.put(selector, element);
        return element;
    }

    @Override
    public void close() throws Exception {
        this.disable();
        client.close();
    }

}
