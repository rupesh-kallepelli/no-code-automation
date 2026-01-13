package com.vr.cdp.protocol.command.target;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class TargetDetachFromTarget
        extends TargetCommand<EmptyResult> {

    private final Params params;

    public TargetDetachFromTarget(String sessionId) {
        super("Target.detachFromTarget");
        this.params = new Params(sessionId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String sessionId) {}
}
