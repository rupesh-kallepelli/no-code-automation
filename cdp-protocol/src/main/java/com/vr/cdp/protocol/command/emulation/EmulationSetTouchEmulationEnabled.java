package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationSetTouchEmulationEnabled
        extends EmulationCommand<EmptyResult> {

    private final Params params;

    public EmulationSetTouchEmulationEnabled(boolean enabled) {
        super("Emulation.setTouchEmulationEnabled");
        this.params = new Params(enabled);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(boolean enabled) {}
}
