package com.vr.cdp.protocol.command.network;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.Map;

public class NetworkContinueInterceptedRequest
        extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkContinueInterceptedRequest(
            String interceptionId,
            String errorReason,
            String rawResponse,
            Map<String, String> headers
    ) {
        super("Network.continueInterceptedRequest");
        this.params = new Params(
                interceptionId, errorReason, rawResponse, headers
        );
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(
            String interceptionId,
            String errorReason,
            String rawResponse,
            Map<String, String> headers
    ) {}
}
