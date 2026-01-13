package com.vr.cdp.protocol.command.page;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class PageReload extends PageCommand<EmptyResult> {

    private final Params params;

    public PageReload(boolean ignoreCache) {
        super("Page.reload");
        this.params = new Params(ignoreCache);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(boolean ignoreCache) {}
}
