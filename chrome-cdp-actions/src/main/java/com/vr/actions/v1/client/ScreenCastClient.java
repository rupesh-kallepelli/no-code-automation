package com.vr.actions.v1.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.cdp.protocol.command.page.PageScreencastFrame;
import com.vr.cdp.protocol.command.page.PageScreencastFrameAck;

public class ScreenCastClient extends PageCDPClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final BroadCaster broadCaster;

    public ScreenCastClient(String wsUrl, BroadCaster broadCaster) throws Exception {
        super(wsUrl);
        this.broadCaster = broadCaster;
    }

    public void setPage(Page page) {
        super.setPage(page);
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);

        try {
            JsonNode root = MAPPER.readTree(message);
            if ("Page.screencastFrame".equals(root.path("method").asText())) {
                PageScreencastFrame frame = MAPPER.treeToValue(root, PageScreencastFrame.class);
                broadCaster.broadcast(frame.params().data());
                send(new PageScreencastFrameAck(frame.params().sessionId()));
            }
        } catch (Exception e) {
            System.err.println("Failed to save screencast frame : " + e);
        }
    }

}


