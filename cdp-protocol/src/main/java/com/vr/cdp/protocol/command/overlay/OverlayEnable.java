package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.CDPCommand;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class OverlayEnable extends CDPCommand<EmptyResult> {

    private final Params params = new Params();

    public OverlayEnable() {
        super("Overlay.enable");
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params() {}
}
