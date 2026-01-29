package com.vr.actions.v1.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.cdp.client.ws.RawCDPClient;

import java.net.URISyntaxException;

public class PageCDPClient extends RawCDPClient {
    private final Page page;

    public PageCDPClient(String wsUrl, Page page) throws URISyntaxException, InterruptedException {
        super(wsUrl);
        this.page = page;
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);
        try {
            JsonNode json = mapper.readTree(message);
            if (json.has("method")) {
                page.onEvent(message);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
