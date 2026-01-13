package com.vr.cdp.protocol.command.fetch;

import com.vr.cdp.protocol.command.response.EmptyResult;

import java.util.List;

public class FetchFulfillRequest
        extends FetchCommand<EmptyResult> {

    private final Params params;

    public FetchFulfillRequest(
            String requestId,
            int responseCode,
            String body,
            List<HeaderEntry> responseHeaders
    ) {
        super("Fetch.fulfillRequest");
        this.params = new Params(requestId, responseCode, responseHeaders, body);
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
            int responseCode,
            List<HeaderEntry> responseHeaders,
            String body
    ) {}

    public record HeaderEntry(String name, String value) {}
}
