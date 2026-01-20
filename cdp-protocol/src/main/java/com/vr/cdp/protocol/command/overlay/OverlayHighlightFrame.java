package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class OverlayHighlightFrame extends OverlayCommand<EmptyResult> {

    private final Params params;

    public OverlayHighlightFrame(String frameId) {
        super("Overlay.highlightFrame");
        this.params = new Params(
                frameId,
                new RGBA(255, 165, 0, 0.8)
        );
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String frameId, RGBA color) {
    }

    public record RGBA(Integer r, Integer g, Integer b, Double a) {
    }
}
