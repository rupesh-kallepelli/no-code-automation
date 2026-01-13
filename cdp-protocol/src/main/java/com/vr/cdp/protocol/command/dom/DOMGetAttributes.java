package com.vr.cdp.protocol.command.dom;

import java.util.List;

public class DOMGetAttributes
        extends DOMCommand<DOMGetAttributes.Result> {

    private final Params params;

    public DOMGetAttributes(int nodeId) {
        super("DOM.getAttributes");
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

    public record Params(int nodeId) {}

    public record Result(List<String> attributes) {}
}
