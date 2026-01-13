package com.vr.cdp.protocol.command.fetch;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class FetchFailRequest
        extends FetchCommand<EmptyResult> {

    private final Params params;

    public FetchFailRequest(String requestId, String errorReason) {
        super("Fetch.failRequest");
        this.params = new Params(requestId, errorReason);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String requestId, String errorReason) {}
}
