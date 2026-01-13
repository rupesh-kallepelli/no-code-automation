package com.vr.cdp.protocol.command.dom;

public class DOMQuerySelector
        extends DOMCommand<DOMQuerySelector.Result> {

    private final Params params;

    public DOMQuerySelector(int nodeId, String selector) {
        super("DOM.querySelector");
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

    public record Result(int nodeId) {}
}
