package com.vr.cdp.protocol.command.page;


public class PageCaptureScreenshot
        extends PageCommand<PageCaptureScreenshot.Result> {

    private final Params params;

    public PageCaptureScreenshot(String format) {
        super("Page.captureScreenshot");
        this.params = new Params(format);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(String format) {
    }

    public record Result(String data) {
    }
}
