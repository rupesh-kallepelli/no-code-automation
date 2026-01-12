package com.vr.ai.automation.cdp;

import com.vr.ai.automation.cdp.CdpClient;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class PageSocket extends WebSocketClient {

    public PageSocket(String wsUrl) throws Exception {
        super(new URI(wsUrl));
        System.out.println(wsUrl);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("✅ Page socket connected");
    }

    @Override
    public void onMessage(String message) {
        if (message.contains("\"id\"")) {
            System.out.println("⬅ PAGE CDP: " + message);
        }
    }

    public void enable() throws Exception {
        sendSafe("Page.enable");
        sendSafe("Runtime.enable");
        sendSafe("DOM.enable");
    }

    public void navigate(String url) throws Exception {
        sendSafe("Page.navigate", CdpClient.params("url", url));
    }

    public void type(String selector, String text) throws Exception {
        String js = """
                const el = document.querySelector("%s");
                if (el) { el.focus(); el.value = "%s"; }
                """.formatted(selector, text);

        sendSafe("Runtime.evaluate",
                CdpClient.params(Map.of("expression", js)));
    }

    public void click(String selector) throws Exception {
        String js = """
                document.querySelector("%s")?.click();
                """.formatted(selector);

        sendSafe("Runtime.evaluate",
                CdpClient.params(Map.of("expression", js)));
    }

    public void waitForVisible(String selector, int timeoutMs) throws Exception {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {
            String js = """
                    document.querySelector("%s") !== null
                    """.formatted(selector);

            sendSafe("Runtime.evaluate",
                    CdpClient.params(Map.of("expression", js, "returnByValue", true)));

            try { Thread.sleep(300); } catch (Exception ignored) {}
        }
        throw new RuntimeException("Timeout waiting for " + selector);
    }

    private void sendSafe(String method) throws Exception {
//        send(CdpClient.command(method, null));
    }

    private void sendSafe(String method, Object params) throws Exception {
//        send(CdpClient.command(method, (com.fasterxml.jackson.databind.node.ObjectNode) params));
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("❌ CDP closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
