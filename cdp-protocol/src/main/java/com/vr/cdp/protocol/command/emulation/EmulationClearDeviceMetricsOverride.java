package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class EmulationClearDeviceMetricsOverride
        extends EmulationCommand<EmptyResult> {

    public EmulationClearDeviceMetricsOverride() {
        super("Emulation.clearDeviceMetricsOverride");
    }

    @Override
    public Object getParams() {
        return EmptyParams.INSTANCE;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }
}
