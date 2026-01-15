package com.vr.cdp.protocol.command.page;

public class PageStopScreencast
        extends PageCommand<Void> {

    public PageStopScreencast() {
        super("Page.stopScreencast");
    }

    @Override
    public Object getParams() {
        return null;
    }

    @Override
    public Class<Void> getResultType() {
        return Void.class;
    }
}
