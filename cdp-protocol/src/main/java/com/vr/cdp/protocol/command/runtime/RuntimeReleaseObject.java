package com.vr.cdp.protocol.command.runtime;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class RuntimeReleaseObject
        extends RuntimeCommand<EmptyResult> {

    private final Params params;

    public RuntimeReleaseObject(String objectId) {
        super("Runtime.releaseObject");
        this.params = new Params(objectId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String objectId) {}
}
