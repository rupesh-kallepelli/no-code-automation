package com.vr.cdp.protocol.command.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NetworkRequestWillBeSent(
        String requestId,
        Request request
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            String url,
            String method
    ) {}
}
