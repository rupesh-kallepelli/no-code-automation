package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationSetUserAgentOverride
        extends EmulationCommand<EmptyResult> {

    private final Params params;

    public EmulationSetUserAgentOverride(String userAgent) {
        super("Emulation.setUserAgentOverride");
        this.params = new Params(userAgent, null, null);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(
            String userAgent,
            String acceptLanguage,
            String platform
    ) {}
}
