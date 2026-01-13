package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationSetLocaleOverride
        extends EmulationCommand<EmptyResult> {

    private final Params params;

    public EmulationSetLocaleOverride(String locale) {
        super("Emulation.setLocaleOverride");
        this.params = new Params(locale);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String locale) {}
}
