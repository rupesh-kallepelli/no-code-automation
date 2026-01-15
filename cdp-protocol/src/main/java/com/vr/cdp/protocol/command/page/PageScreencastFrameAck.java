package com.vr.cdp.protocol.command.page;

public class PageScreencastFrameAck
        extends PageCommand<Void> {

    private final Params params;

    public PageScreencastFrameAck(int sessionId) {
        super("Page.screencastFrameAck");
        this.params = new Params(sessionId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Void> getResultType() {
        return Void.class;
    }

    public record Params(int sessionId) {}
}
