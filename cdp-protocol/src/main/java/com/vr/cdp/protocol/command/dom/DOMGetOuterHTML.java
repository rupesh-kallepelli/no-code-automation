package com.vr.cdp.protocol.command.dom;

public class DOMGetOuterHTML extends DOMCommand<DOMGetOuterHTML.Result> {

    private final Params params;

    public DOMGetOuterHTML(Integer nodeId) {
        super("DOM.getOuterHTML");
        this.params = new Params(nodeId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(Integer nodeId) {
    }

    public record Result(String outerHTML) {
    }
}
