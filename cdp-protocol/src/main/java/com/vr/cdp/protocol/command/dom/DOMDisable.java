package com.vr.cdp.protocol.command.dom;


import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class DOMDisable extends DOMCommand<EmptyResult> {

    public DOMDisable() {
        super("DOM.disable");
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
