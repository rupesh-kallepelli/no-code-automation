package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationSetTimezoneOverride
        extends EmulationCommand<EmptyResult> {

    private final Params params;

    public EmulationSetTimezoneOverride(String timezoneId) {
        super("Emulation.setTimezoneOverride");
        this.params = new Params(timezoneId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String timezoneId) {}
}
