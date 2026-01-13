package com.vr.cdp.protocol.command.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NetworkResponseReceived(
        String requestId,
        Response response
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            int status,
            String statusText,
            String mimeType
    ) {}
}
