package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class OverlayHideHighlight extends OverlayCommand<EmptyResult> {

    private final Params params = new Params();

    public OverlayHideHighlight() {
        super("Overlay.hideHighlight");
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
