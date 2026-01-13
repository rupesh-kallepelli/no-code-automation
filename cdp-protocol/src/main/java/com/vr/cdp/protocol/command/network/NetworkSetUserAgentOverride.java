package com.vr.cdp.protocol.command.network;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class NetworkSetUserAgentOverride
        extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkSetUserAgentOverride(String userAgent) {
        super("Network.setUserAgentOverride");
        this.params = new Params(userAgent);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String userAgent) {}
}
