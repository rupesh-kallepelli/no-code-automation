package com.vr.cdp.protocol.command.page;

public class PageStartScreencast
        extends PageCommand<Void> {

    private final Params params;

    public PageStartScreencast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight) {

        super("Page.startScreencast");
        this.params = new Params(format, quality, maxWidth, maxHeight);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Void> getResultType() {
        return Void.class;
    }

    public record Params(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    ) {
    }
}
