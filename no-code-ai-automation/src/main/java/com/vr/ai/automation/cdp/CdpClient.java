package com.vr.ai.automation.cdp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class CdpClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final AtomicInteger ID = new AtomicInteger(1);

    private CdpClient() {
    }

    public static ObjectNode command(String method, ObjectNode params) {
        ObjectNode cmd = MAPPER.createObjectNode();
        cmd.put("id", ID.getAndIncrement());
        cmd.put("method", method);
        if (params != null) cmd.set("params", params);
        return cmd;
    }

    public static ObjectNode params(String key, String value) {
        ObjectNode n = MAPPER.createObjectNode();
        n.put(key, value);
        return n;
    }

    public static ObjectNode params(String key, boolean value) {
        ObjectNode n = MAPPER.createObjectNode();
        n.put(key, value);
        return n;
    }

    public static ObjectNode params(String key, int value) {
        ObjectNode n = MAPPER.createObjectNode();
        n.put(key, value);
        return n;
    }

    public static ObjectNode params(Map<String, Object> params) {
        ObjectNode node = MAPPER.createObjectNode();

        for (var e : params.entrySet()) {
            Object v = e.getValue();
            String k = e.getKey();

            if (v == null) node.putNull(k);
            else if (v instanceof String s) node.put(k, s);
            else if (v instanceof Boolean b) node.put(k, b);
            else if (v instanceof Integer i) node.put(k, i);
            else if (v instanceof Long l) node.put(k, l);
            else if (v instanceof Double d) node.put(k, d);
            else if (v instanceof Float f) node.put(k, f);
            else if (v instanceof Map<?, ?> m) node.set(k, MAPPER.valueToTree(m));
            else if (v instanceof List<?> l) node.set(k, MAPPER.valueToTree(l));
            else if (v instanceof ObjectNode o) node.set(k, o);
            else
                throw new IllegalArgumentException("Unsupported param type: " + v.getClass());
        }
        return node;
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    public static int nextId() {
        return ID.getAndIncrement();
    }
}

