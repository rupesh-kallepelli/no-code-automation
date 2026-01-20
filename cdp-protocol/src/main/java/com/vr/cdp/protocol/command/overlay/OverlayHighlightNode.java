package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class OverlayHighlightNode extends OverlayCommand<EmptyResult> {

    private final Params params;

    public OverlayHighlightNode(Integer nodeId) {
        super("Overlay.highlightNode");
        this.params = new Params(
                nodeId,
                new HighlightConfig(
                        new RGBA(255, 0, 0, 1.0),
                        new RGBA(0, 255, 0, 0.2),
                        true,
                        true,
                        false,
                        false
                )
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

    /* ---------- Params Records ---------- */

    public record Params(
            Integer nodeId,
            HighlightConfig highlightConfig
    ) {
    }

    public record HighlightConfig(
            RGBA borderColor,
            RGBA contentColor,
            Boolean showInfo,
            Boolean showStyles,
            Boolean showRulers,
            Boolean showExtensionLines
    ) {
    }

    public record RGBA(
            Integer r,
            Integer g,
            Integer b,
            Double a
    ) {
    }
}
