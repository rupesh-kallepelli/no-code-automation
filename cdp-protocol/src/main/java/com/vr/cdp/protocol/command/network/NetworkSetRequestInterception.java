package com.vr.cdp.protocol.command.network;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.List;

public class NetworkSetRequestInterception
        extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkSetRequestInterception(List<Pattern> patterns) {
        super("Network.setRequestInterception");
        this.params = new Params(patterns);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(List<Pattern> patterns) {}

    public record Pattern(
            String urlPattern,
            String resourceType,
            String interceptionStage
    ) {}
}
