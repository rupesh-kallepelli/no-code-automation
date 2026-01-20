package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class OverlayDisable extends OverlayCommand<EmptyResult> {

    private final Params params = new Params();

    public OverlayDisable() {
        super("Overlay.disable");
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params() {
    }
}
