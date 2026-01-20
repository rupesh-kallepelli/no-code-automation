package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.CDPCommand;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class OverlaySetInspectMode extends OverlayCommand<EmptyResult> {

    private final Params params;

    public OverlaySetInspectMode(String mode) {
        super("Overlay.setInspectMode");
        this.params = new Params(
            mode,
            new HighlightConfig(
                new RGBA(0, 0, 255, 1.0),
                new RGBA(0, 0, 255, 0.15),
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

    /* ---------- Records ---------- */

    public record Params(
        String mode,
        HighlightConfig highlightConfig
    ) {}

    public record HighlightConfig(
        RGBA borderColor,
        RGBA contentColor,
        Boolean showInfo
    ) {}

    public record RGBA(
        Integer r,
        Integer g,
        Integer b,
        Double a
    ) {}
}
