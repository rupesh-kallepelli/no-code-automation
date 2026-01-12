package com.vr.ai.automation.session;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vr.ai.automation.cdp.BrowserType;
import com.vr.ai.automation.cdp.CdpClient;
import com.vr.ai.automation.cdp.ChromiumBinaryResolver;
import com.vr.ai.automation.cdp.ChromiumLauncher;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class CdpBrowserSession implements BrowserSession {

    private WebSocketClient socket;

    // =========================
    // Constructor
    // =========================
    public CdpBrowserSession(
            BrowserType browserType,
            String binaryOverride,
            boolean headless
    ) {
        try {
            String binary =
                    ChromiumBinaryResolver.resolve(browserType, binaryOverride);

            ChromiumLauncher.launch(binary, headless, 1000);

            Thread.sleep(2000);

//            String wsUrl = CdpClient.getWebSocketDebuggerUrl();
//            connect(wsUrl);
            send("Runtime.disable");
            enableDomains();

        } catch (Exception e) {
            throw new RuntimeException("Failed to start CDP session", e);
        }
    }

    // =========================
    // CDP CONNECTION (PRIVATE)
    // =========================
    private void connect(String wsUrl) throws Exception {
        socket = new WebSocketClient(new URI(wsUrl)) {

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("✅ CDP connected");
            }

            @Override
            public void onMessage(String message) {
                if (message.contains("\"id\"")) {
                    System.out.println("⬅ CDP: " + message);
                }
//                System.out.println("⬅ CDP: " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("❌ CDP closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };

        socket.connect();
        while (!socket.isOpen()) {
            Thread.sleep(50);
        }

    }

    // =========================
    // CDP DOMAIN ENABLEMENT
    // =========================
    private void enableDomains() throws Exception {
        send("Page.enable");
//        send("DOM.enable");
//        send("Runtime.enable");
//        send("Input.enable");
    }

    // =========================
    // CDP SEND HELPERS
    // =========================
    private void send(String method) throws Exception {
//        String command = CdpClient.command(method, null);
//        System.out.println(command);
//        socket.send(command);
    }

    private void send(String method, ObjectNode params) throws Exception {
//        String command = CdpClient.command(method, params);
//        System.out.println(command);
//        socket.send(command);
    }


    // =========================
    // BrowserSession METHODS
    // =========================
    @Override
    public void navigate(String url) {
        try {
            send("Page.navigate",
                    CdpClient.params("url", url));
            waitForPageReady(50000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForPageReady(int timeoutMs) throws InterruptedException {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {
            // just sleep, CDP events will come asynchronously
            Thread.sleep(100);
        }
    }

    @Override
    public void type(String selector, String text) {
        System.out.println("CDP → Type '" + text + "' into " + selector);
    }

    @Override
    public void click(String selector) {
        System.out.println("CDP → Click " + selector);
    }

    @Override
    public void waitForVisible(String selector, int timeoutMs) {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {
            try {
                String expression = """
                            document.querySelector("%s") !== null
                        """.formatted(selector);

                send("Runtime.evaluate",
                        CdpClient.params(
                                Map.of("expression", expression,
                                        "returnByValue", true)
                        ));

                Thread.sleep(500); // 🔥 IMPORTANT throttle
                return; // optimistic success for now

            } catch (Exception ignored) {
            }
        }

        throw new RuntimeException("Timeout waiting for: " + selector);
    }


    @Override
    public void close() {
        System.out.println("Closing browser session");
    }
}
