package com.vr.actions.v1.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.cdp.client.ws.RawCDPClient;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageCDPClient extends RawCDPClient {

    private Page page;
    private String sessionId;

    public PageCDPClient(String wsUrl) throws URISyntaxException, InterruptedException {
        super(wsUrl);
    }

    public void setPage(Page page) {
        this.page = page;
        this.sessionId = page.getSessionId();
    }

    @Override
    public String createRequestJson(int command, String method, Object params) throws JsonProcessingException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", command);
        paramsMap.put("method", method);
        paramsMap.put("params", params);
        if (Objects.nonNull(sessionId)) paramsMap.put("sessionId", sessionId);
        return mapper.writeValueAsString(paramsMap);
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);
        try {
            JsonNode json = mapper.readTree(message);
            if (json.has("method")) {
                if (Objects.nonNull(page))
                    page.onEvent(message);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
