package com.vr.cdp.protocol.command.target;

import java.util.List;

public class TargetGetTargets
        extends TargetCommand<TargetGetTargets.Result> {

    public TargetGetTargets() {
        super("Target.getTargets");
    }

    @Override
    public Object getParams() {
        return null;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Result(List<TargetInfo> targetInfos) {}

    public record TargetInfo(
            String targetId,
            String type,
            String title,
            String url,
            boolean attached,
            String openerId
    ) {}
}
