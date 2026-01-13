package com.vr.cdp.protocol.command.input;


import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class InputImeCancelComposition extends InputCommand<EmptyResult> {

    public InputImeCancelComposition() {
        super("Input.imeCancelComposition");
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
