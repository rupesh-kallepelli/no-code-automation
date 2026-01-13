package com.vr.cdp.protocol.command.target;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class TargetActivateTarget
        extends TargetCommand<EmptyResult> {

    private final Params params;

    public TargetActivateTarget(String targetId) {
        super("Target.activateTarget");
        this.params = new Params(targetId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String targetId) {}
}
