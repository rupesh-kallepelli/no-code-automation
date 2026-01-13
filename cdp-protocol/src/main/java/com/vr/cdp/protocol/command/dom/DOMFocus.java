package com.vr.cdp.protocol.command.dom;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class DOMFocus extends DOMCommand<EmptyResult> {

    private final Params params;

    public DOMFocus(int nodeId) {
        super("DOM.focus");
        this.params = new Params(nodeId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(int nodeId) {}
}
