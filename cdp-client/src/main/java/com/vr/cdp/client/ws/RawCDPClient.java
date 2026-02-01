package com.vr.cdp.client.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.CDPResponse;
import com.vr.cdp.protocol.command.CDPCommand;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class RawCDPClient extends WebSocketClient implements CDPClient {

    protected final ObjectMapper mapper = new ObjectMapper();
    private final AtomicInteger idGen = new AtomicInteger(1);

    private final Map<Integer, String> responses = new ConcurrentHashMap<>();
    private final Map<String, Consumer<String>> eventHandlers = new ConcurrentHashMap<>();


    public RawCDPClient(String wsUrl) throws URISyntaxException, InterruptedException {
        super(new URI(wsUrl));
        connectBlocking();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("✅ CDP WebSocket connected");
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonNode json = mapper.readTree(message);

            System.out.println("==================================Event triggered by cdp==================================");
            System.out.println(message);
            // Event
            if (json.has("method")) {
                String method = json.get("method").asText();
                Consumer<String> handler = eventHandlers.get(method);
                if (handler != null) {
                    handler.accept(message);
                }
                return;
            }

            // Response
            if (json.has("id")) {
                int id = json.get("id").asInt();
                responses.put(id, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("❌ CDP socket closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void send(CDPCommand<?> command) throws Exception {
        int id = idGen.getAndIncrement();
        command.setId(id);
        send(createRequestJson(command.getId(), command.getMethod(), command.getParams()));
    }

    public String createRequestJson(int command, String method, Object params) throws JsonProcessingException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", command);
        paramsMap.put("method", method);
        paramsMap.put("params", params);
        return mapper.writeValueAsString(paramsMap);
    }

    @Override
    public <R> R sendAndWait(CDPCommand<R> command) throws Exception {
        int id = idGen.getAndIncrement();
        command.setId(id);
        String json = createRequestJson(command.getId(), command.getMethod(), command.getParams());

        System.out.println("==================================Sending command==================================");
        System.out.println(json);

        send(json);
        // Busy-wait (simple, deterministic)
        while (!responses.containsKey(id)) {
            Thread.sleep(5);
        }

        String responseJson = responses.remove(id);
        CDPResponse<R> response = mapper.readValue(responseJson, mapper.getTypeFactory().constructParametricType(CDPResponse.class, command.getResultType()));

        if (response.getError() != null) {
            throw new RuntimeException("CDP Error " + response.getError().getCode() + ": " + response.getError().getMessage());
        }
        return response.getResult();
    }

    @Override
    public void onEvent(String method, Consumer<String> handler) {
        eventHandlers.put(method, handler);
    }
}
