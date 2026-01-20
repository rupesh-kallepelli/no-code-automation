package com.vr.cdp.protocol.command.dom;

import java.util.List;

public class DOMGetSearchResults extends DOMCommand<DOMGetSearchResults.Result> {
    private Params params;

    public DOMGetSearchResults(String searchId, Integer count) {
        super("DOM.getSearchResults");
        this.params = new Params(searchId, 0, count);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(String searchId, Integer fromIndex, Integer toIndex) {
    }

    public record Result(List<Integer> nodeIds) {
    }
}
