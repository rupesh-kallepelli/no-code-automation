package com.vr.cdp.protocol.command.page;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class PageStopLoading extends PageCommand<EmptyResult> {

    public PageStopLoading() {
        super("Page.stopLoading");
    }

    @Override
    public Object getParams() {
        return null;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }
}
