package com.vr.cdp.protocol.command.target;

public class TargetCloseTarget
        extends TargetCommand<TargetCloseTarget.Result> {

    private final Params params;

    public TargetCloseTarget(String targetId) {
        super("Target.closeTarget");
        this.params = new Params(targetId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(String targetId) {}

    public record Result(boolean success) {}
}
