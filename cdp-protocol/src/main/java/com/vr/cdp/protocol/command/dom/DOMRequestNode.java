package com.vr.cdp.protocol.command.dom;

public class DOMRequestNode extends DOMCommand<DOMRequestNode.Result> {

    private final Params params;

    public DOMRequestNode(String selector) {
        super("DOM.requestNode");
        this.params = new Params(selector);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result>

    getResultType() {
        return Result.class;
    }

    public record Params(String objectId) {
    }

    public record Result(Integer nodeId) {
    }
}