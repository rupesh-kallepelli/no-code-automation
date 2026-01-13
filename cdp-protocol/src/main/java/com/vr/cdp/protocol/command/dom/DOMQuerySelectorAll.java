package com.vr.cdp.protocol.command.dom;

import java.util.List;

public class DOMQuerySelectorAll
        extends DOMCommand<DOMQuerySelectorAll.Result> {

    private final Params params;

    public DOMQuerySelectorAll(int nodeId, String selector) {
        super("DOM.querySelectorAll");
        this.params = new Params(nodeId, selector);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(int nodeId, String selector) {}

    public record Result(List<Integer> nodeIds) {}
}
