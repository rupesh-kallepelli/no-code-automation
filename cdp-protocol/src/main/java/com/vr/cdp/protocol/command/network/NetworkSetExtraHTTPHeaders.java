package com.vr.cdp.protocol.command.network;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.Map;

public class NetworkSetExtraHTTPHeaders
        extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkSetExtraHTTPHeaders(Map<String, String> headers) {
        super("Network.setExtraHTTPHeaders");
        this.params = new Params(headers);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(Map<String, String> headers) {}
}
