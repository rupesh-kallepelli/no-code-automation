package com.vr.cdp.protocol.command.target;

public class TargetAttachToTarget
        extends TargetCommand<TargetAttachToTarget.Result> {

    private final Params params;

    public TargetAttachToTarget(String targetId, boolean flatten) {
        super("Target.attachToTarget");
        this.params = new Params(targetId, flatten);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(String targetId, boolean flatten) {}

    public record Result(String sessionId) {}
}
