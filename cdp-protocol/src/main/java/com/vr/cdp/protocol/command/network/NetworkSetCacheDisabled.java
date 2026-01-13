package com.vr.cdp.protocol.command.network;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class NetworkSetCacheDisabled
        extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkSetCacheDisabled(boolean cacheDisabled) {
        super("Network.setCacheDisabled");
        this.params = new Params(cacheDisabled);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(boolean cacheDisabled) {}
}
