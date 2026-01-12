package com.vr.ai.automation.cdp;

import com.fasterxml.jackson.databind.JsonNode;
import com.vr.ai.automation.session.BrowserSession;

import java.util.Map;

public class CdpBrowserSessionV2 implements BrowserSession {

    private final BrowserSocket socket;

    public CdpBrowserSessionV2(
            BrowserType browserType,
            String binaryOverride,
            boolean headless
    ) {
        try {
            String binary = ChromiumBinaryResolver.resolve(browserType, binaryOverride);
            ChromiumLauncher.launch(binary, headless, 1000);
            Thread.sleep(2000);

            socket = new BrowserSocket(BrowserSocket.resolveBrowserWs());
            socket.connectBlocking();

            socket.send(
                    CdpClient.command(
                            "Target.createTarget",
                            CdpClient.params("url", "about:blank")
                    ).toString()
            );

            socket.waitForPageSession();

            socket.sendToPage("Page.enable", null);
            socket.sendToPage("Runtime.enable", null);
            socket.sendToPage("DOM.enable", null);

        } catch (Exception e) {
            throw new RuntimeException("CDP start failed", e);
        }
    }

    @Override
    public void navigate(String url) throws Exception {
        socket.resetPageLoad();
        socket.sendToPage("Page.navigate", CdpClient.params("url", url));

        long start = System.currentTimeMillis();
        while (!socket.isPageLoaded() && System.currentTimeMillis() - start < 15000) {
            Thread.sleep(50);
        }
    }

    @Override
    public void type(String rawSelector, String text) throws Exception {
        String selector = normalize(rawSelector).replace("'", "\\'");

        socket.sendToPageAndWait(
                "Runtime.evaluate",
                CdpClient.params(Map.of(
                        "expression", "document.querySelector('" + selector + "')?.focus()"
                ))
        );

        for (char c : text.toCharArray()) {
            socket.sendToPage("Input.dispatchKeyEvent",
                    CdpClient.params(Map.of("type", "keyDown", "text", String.valueOf(c))));
            socket.sendToPage("Input.dispatchKeyEvent",
                    CdpClient.params(Map.of("type", "keyUp", "text", String.valueOf(c))));
        }
    }

    @Override
    public void click(String rawSelector) throws Exception {
        String selector = normalize(rawSelector).replace("'", "\\'");

        String js = """
            (() => {
              const el = document.querySelector('%s');
              if (!el) return null;
              const r = el.getBoundingClientRect();
              return { x: r.left + r.width/2, y: r.top + r.height/2 };
            })()
            """.formatted(selector);

        JsonNode r = socket.sendToPageAndWait(
                "Runtime.evaluate",
                CdpClient.params(Map.of(
                        "expression", js,
                        "returnByValue", true
                ))
        );

        JsonNode v = r.path("result").path("result").path("value");
        if (v.isNull()) throw new RuntimeException("Element not found");

        double x = v.get("x").asDouble();
        double y = v.get("y").asDouble();

        socket.sendToPage("Input.dispatchMouseEvent",
                CdpClient.params(Map.of(
                        "type", "mousePressed",
                        "x", x,
                        "y", y,
                        "button", "left",
                        "clickCount", 1
                )));

        socket.sendToPage("Input.dispatchMouseEvent",
                CdpClient.params(Map.of(
                        "type", "mouseReleased",
                        "x", x,
                        "y", y,
                        "button", "left",
                        "clickCount", 1
                )));
    }

    @Override
    public void waitForVisible(String rawSelector, int timeoutMs) throws Exception {
        String selector = normalize(rawSelector).replace("'", "\\'");

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            boolean visible = socket.evalBoolean("""
                (() => {
                  const el = document.querySelector('%s');
                  if (!el) return false;
                  const r = el.getBoundingClientRect();
                  return r.width > 0 && r.height > 0;
                })()
                """.formatted(selector));

            if (visible) return;
            Thread.sleep(200);
        }
        throw new RuntimeException("Timeout waiting for " + selector);
    }

    @Override
    public void close() {
        socket.close();
    }

    private String normalize(Object s) {
        if (s instanceof String str) {
            if (str.startsWith("id=")) return "#" + str.substring(3);
            if (str.startsWith("name=")) return "[name=\"" + str.substring(5) + "\"]";
            return str;
        }
        throw new IllegalArgumentException("Invalid selector");
    }
}
