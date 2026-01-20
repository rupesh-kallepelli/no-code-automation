package com.vr.cdp.protocol.command.dom;

public class DOMPerformSearch
        extends DOMCommand<DOMPerformSearch.Result> {

    private final Params params;

    public DOMPerformSearch(String selector) {
        super("DOM.performSearch");
        this.params = new Params(selector, true);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(String query, boolean includeUserAgentShadowDOM) {
    }

    public record Result(String searchId, Integer resultCount) {
    }
}