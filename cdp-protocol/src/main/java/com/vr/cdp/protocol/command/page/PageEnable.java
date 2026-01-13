package com.vr.cdp.protocol.command.page;


import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class PageEnable extends PageCommand<EmptyResult> {

    public PageEnable() {
        super("Page.enable");
    }

    @Override
    public Object getParams() {
        return EmptyParams.INSTANCE;
    }


    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }
}
