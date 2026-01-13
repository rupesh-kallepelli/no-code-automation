package com.vr.cdp.protocol.command.page;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class PageNavigateToHistoryEntry
        extends PageCommand<EmptyResult> {

    private final Params params;

    public PageNavigateToHistoryEntry(int entryId) {
        super("Page.navigateToHistoryEntry");
        this.params = new Params(entryId);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(int entryId) {}
}
