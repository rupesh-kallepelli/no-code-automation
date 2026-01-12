package com.vr.ai.automation.cdp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BrowserSocket extends WebSocketClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private volatile String pageSessionId;
    private volatile boolean pageLoaded = false;

    private final AtomicInteger idGen = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, CompletableFuture<JsonNode>> pending =
            new ConcurrentHashMap<>();

    public static String resolveBrowserWs() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:1000/json/version"))
                .GET()
                .build();

        HttpResponse<String> res =
                HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

        return MAPPER.readTree(res.body())
                .get("webSocketDebuggerUrl").asText();
    }

    public BrowserSocket(String ws) throws Exception {
        super(new URI(ws));
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        send(CdpClient.command(
                "Target.setAutoAttach",
                CdpClient.params(Map.of(
                        "autoAttach", true,
                        "waitForDebuggerOnStart", false,
                        "flatten", true
                ))
        ).toString());
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        try {
            JsonNode json = MAPPER.readTree(message);

            if (json.has("id")) {
                CompletableFuture<JsonNode> f = pending.remove(json.get("id").asInt());
                if (f != null) f.complete(json);
                return;
            }

            if ("Page.loadEventFired".equals(json.path("method").asText())) {
                pageLoaded = true;
            }

            if ("Target.attachedToTarget".equals(json.path("method").asText())) {

                JsonNode info = json.path("params").path("targetInfo");

                if ("page".equals(info.path("type").asText())) {
                    String url = info.path("url").asText();

                    // 🔥 IGNORE about:blank
                    if (!url.isEmpty() && !"about:blank".equals(url)) {
                        pageSessionId = json.path("params").path("sessionId").asText();
                        System.out.println("✅ Bound to page: " + url);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitForPageSession() throws InterruptedException {
        long start = System.currentTimeMillis();
        while (pageSessionId == null && System.currentTimeMillis() - start < 5000) {
            Thread.sleep(50);
        }
        if (pageSessionId == null)
            throw new RuntimeException("Page session not attached");
    }

    public boolean isPageLoaded() {
        return pageLoaded;
    }

    public void resetPageLoad() {
        pageLoaded = false;
    }

    public JsonNode sendToPageAndWait(String method, ObjectNode params) throws Exception {
        int id = idGen.getAndIncrement();
        CompletableFuture<JsonNode> future = new CompletableFuture<>();
        pending.put(id, future);

        ObjectNode cmd = MAPPER.createObjectNode();
        cmd.put("id", id);
        cmd.put("method", method);
        cmd.put("sessionId", pageSessionId);
        if (params != null) cmd.set("params", params);

        send(cmd.toString());
        return future.get(10, TimeUnit.SECONDS);
    }

    public void sendToPage(String method, ObjectNode params) throws Exception {
        ObjectNode cmd = MAPPER.createObjectNode();
        cmd.put("id", idGen.getAndIncrement());
        cmd.put("method", method);
        cmd.put("sessionId", pageSessionId);
        if (params != null) cmd.set("params", params);
        send(cmd.toString());
    }

    public boolean evalBoolean(String js) throws Exception {
        JsonNode r = sendToPageAndWait(
                "Runtime.evaluate",
                CdpClient.params(Map.of(
                        "expression", js,
                        "returnByValue", true
                ))
        );

        return r.path("result").path("result").path("value").asBoolean(false);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
