package com.vr.cdp.protocol.command.page;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PageCaptureScreenshot
        extends PageCommand<PageCaptureScreenshot.Result> {

    private final Params params;

    // Default: PNG, viewport only
    public PageCaptureScreenshot() {
        super("Page.captureScreenshot");
        this.params = new Params("png", null, null, true, false);
    }

    // Format only
    public PageCaptureScreenshot(String format) {
        super("Page.captureScreenshot");
        this.params = new Params(format, null, null, true, false);
    }

    // Full page screenshot
    public PageCaptureScreenshot(boolean captureBeyondViewport) {
        super("Page.captureScreenshot");
        this.params = new Params("png", null, null, true, captureBeyondViewport);
    }

    // Element / clipped screenshot
    public PageCaptureScreenshot(
            String format,
            Number x,
            Number y,
            Number width,
            Number height
    ) {
        super("Page.captureScreenshot");
        this.params = new Params(
                format,
                null,
                new Clip(x, y, width, height, 1.0),
                true,
                false
        );
    }

    // Full control (advanced use)
    public PageCaptureScreenshot(
            String format,
            Integer quality,
            Number x,
            Number y,
            Number width,
            Number height,
            Boolean fromSurface,
            Boolean captureBeyondViewport
    ) {
        super("Page.captureScreenshot");
        this.params = new Params(
                format,
                quality,
                (x != null && y != null && width != null && height != null)
                        ? new Clip(x, y, width, height, 1.0)
                        : null,
                fromSurface,
                captureBeyondViewport
        );
    }

    // ============================
    // CDP plumbing
    // ============================

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record Params(
            String format,                     // png | jpeg | webp
            Integer quality,                   // JPEG only
            Clip clip,                         // Optional region
            Boolean fromSurface,               // Default true
            Boolean captureBeyondViewport      // Full page
    ) {
    }

    private record Clip(
            Number x,
            Number y,
            Number width,
            Number height,
            Number scale
    ) {
    }


    public record Result(
            String data     // Base64 image
    ) {
    }
}