package com.vr.cdp.protocol.command.network;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class NetworkEnable extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkEnable(Integer maxTotalBufferSize,
                         Integer maxResourceBufferSize) {
        super("Network.enable");
        this.params = new Params(
                maxTotalBufferSize,
                maxResourceBufferSize
        );
    }

    public NetworkEnable() {
        this(null, null);
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
            Integer maxTotalBufferSize,
            Integer maxResourceBufferSize
    ) {}
}
