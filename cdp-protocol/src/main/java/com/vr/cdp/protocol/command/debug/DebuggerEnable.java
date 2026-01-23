package com.vr.cdp.protocol.command.debug;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class DebuggerEnable extends DebugCommand<EmptyResult> {

    public DebuggerEnable() {
        super("Debugger.enable");
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
