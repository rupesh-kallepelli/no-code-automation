package com.vr.cdp.protocol.command.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PageScreencastFrame(
        String method,
        Params params
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Params(
            String data,
            int sessionId,
            Metadata metadata
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Metadata(
            double timestamp,
            int deviceWidth,
            int deviceHeight,
            double scale
    ) {
    }
}

