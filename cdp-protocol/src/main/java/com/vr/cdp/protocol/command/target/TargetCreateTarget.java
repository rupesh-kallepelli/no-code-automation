package com.vr.cdp.protocol.command.target;

public class TargetCreateTarget
        extends TargetCommand<TargetCreateTarget.Result> {

    private final Params params;

    public TargetCreateTarget(String url) {
        super("Target.createTarget");
        this.params = new Params(url);
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
            String url,
            Integer width,
            Integer height,
            String browserContextId
    ) {
        public Params(String url) {
            this(url, null, null, null);
        }
    }

    public record Result(String targetId) {}
}
