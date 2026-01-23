package com.vr.cdp.protocol.command.debug;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class DebuggerPause extends DebugCommand<EmptyResult> {

    public DebuggerPause() {
        super("Debugger.pause");
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
