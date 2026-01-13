package com.vr.cdp.protocol.command.fetch;

import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class FetchDisable extends FetchCommand<EmptyResult> {

    public FetchDisable() {
        super("Fetch.disable");
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
