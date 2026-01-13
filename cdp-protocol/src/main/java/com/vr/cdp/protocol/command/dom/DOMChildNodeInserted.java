package com.vr.cdp.protocol.command.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DOMChildNodeInserted(
        int parentNodeId,
        Node node
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Node(int nodeId, String nodeName) {}
}
