package com.vr.cdp.protocol.command.dom;

public class DOMGetAttributes extends DOMCommand<DOMGetAttributes.Result> {

    private final Params params;

    public DOMGetAttributes(Integer nodeId) {
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

    /* ---------- Records ---------- */

    public record Params(Integer nodeId) {}

    /**
     * Returned as a flat list:
     * [name1, value1, name2, value2, ...]
     */
    public record Result(java.util.List<String> attributes) {}
}
