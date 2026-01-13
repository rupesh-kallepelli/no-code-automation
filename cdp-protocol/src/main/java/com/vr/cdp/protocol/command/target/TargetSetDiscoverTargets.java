package com.vr.cdp.protocol.command.target;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class TargetSetDiscoverTargets
        extends TargetCommand<EmptyResult> {

    private final Params params;

    public TargetSetDiscoverTargets(boolean discover) {
        super("Target.setDiscoverTargets");
        this.params = new Params(discover);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(boolean discover) {}
}
