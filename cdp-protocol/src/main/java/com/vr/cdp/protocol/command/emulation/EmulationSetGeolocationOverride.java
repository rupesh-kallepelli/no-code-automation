package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationSetGeolocationOverride
        extends EmulationCommand<EmptyResult> {

    private final Params params;

    public EmulationSetGeolocationOverride(
            double latitude,
            double longitude,
            double accuracy
    ) {
        super("Emulation.setGeolocationOverride");
        this.params = new Params(latitude, longitude, accuracy);
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
            double latitude,
            double longitude,
            double accuracy
    ) {}
}
