package com.vr.cdp.protocol.command.fetch;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.List;

public class FetchEnable extends FetchCommand<EmptyResult> {

    private final Params params;

    public FetchEnable(List<RequestPattern> patterns, Boolean handleAuthRequests) {
        super("Fetch.enable");
        this.params = new Params(patterns, handleAuthRequests);
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
            List<RequestPattern> patterns,
            Boolean handleAuthRequests
    ) {}

    public record RequestPattern(
            String urlPattern,
            String resourceType,
            String requestStage
    ) {}
}
