package com.vr.cdp.protocol.command.runtime;


import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class RuntimeEnable extends RuntimeCommand<EmptyResult> {

    public RuntimeEnable() {
        super("Runtime.enable");
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
