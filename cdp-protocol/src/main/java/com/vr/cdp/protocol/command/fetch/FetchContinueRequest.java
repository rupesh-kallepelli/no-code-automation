package com.vr.cdp.protocol.command.fetch;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.List;

public class FetchContinueRequest
        extends FetchCommand<EmptyResult> {

    private final Params params;

    public FetchContinueRequest(String requestId) {
        super("Fetch.continueRequest");
        this.params = new Params(requestId, null, null, null, null);
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
            String requestId,
            String url,
            String method,
            String postData,
            List<HeaderEntry> headers
    ) {}

    public record HeaderEntry(String name, String value) {}
}
