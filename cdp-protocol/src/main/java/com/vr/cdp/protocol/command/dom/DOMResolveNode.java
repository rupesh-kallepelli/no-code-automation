package com.vr.cdp.protocol.command.dom;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DOMResolveNode extends DOMCommand<DOMResolveNode.Result> {

    private final Params params;

    public DOMResolveNode(Integer nodeId, boolean isBackendNodeId) {
        super("DOM.resolveNode");
        if (!isBackendNodeId)
            this.params = new Params(nodeId, null, null);
        else this.params = new Params(null, nodeId, null);
    }


    public DOMResolveNode(Integer nodeId, Integer executionContextId) {
        super("DOM.resolveNode");
        this.params = new Params(nodeId, null, executionContextId);
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Params(
            Integer nodeId,
            Integer backendNodeId,
            Integer executionContextId
    ) {
    }

    public record Result(RemoteObject object) {
    }

    public record RemoteObject(
            String type,
            String subtype,
            String className,
            String description,
            String objectId
    ) {
    }
}
