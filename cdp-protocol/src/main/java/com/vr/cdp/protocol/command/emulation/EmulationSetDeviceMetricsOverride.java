package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationSetDeviceMetricsOverride
        extends EmulationCommand<EmptyResult> {

    private final Params params;

    public EmulationSetDeviceMetricsOverride(
            int width,
            int height,
            double deviceScaleFactor,
            boolean mobile
    ) {
        super("Emulation.setDeviceMetricsOverride");
        this.params = new Params(width, height, deviceScaleFactor, mobile);
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
            int width,
            int height,
            double deviceScaleFactor,
            boolean mobile
    ) {}
}
