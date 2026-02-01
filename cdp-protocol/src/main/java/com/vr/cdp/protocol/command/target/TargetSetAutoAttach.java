package com.vr.cdp.protocol.command.target;

public class TargetSetAutoAttach extends TargetCommand<TargetSetAutoAttach.Result> {

    private final Params params;

    public TargetSetAutoAttach(boolean autoAttach,
                               boolean waitForDebuggerOnStart,
                               boolean flatten) {
        super("Target.setAutoAttach");
        this.params = new Params(autoAttach, waitForDebuggerOnStart, flatten);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(
            boolean autoAttach,
            boolean waitForDebuggerOnStart,
            boolean flatten
    ) {}

    public record Result() {}
}
