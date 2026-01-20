package com.vr.cdp.protocol.command.dom;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class DOMScrollIntoViewIfNeeded extends DOMCommand<EmptyResult> {
    private final Params params;

    public DOMScrollIntoViewIfNeeded(int nodeId) {
        super("DOM.scrollIntoViewIfNeeded");
        this.params = new Params(nodeId);
    }

    @Override
    public Params getParams() {
        return params;
    }

    public record Params(int nodeId) {
    }


    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }
}

