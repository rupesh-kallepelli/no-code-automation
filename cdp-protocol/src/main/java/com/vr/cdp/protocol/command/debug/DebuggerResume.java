package com.vr.cdp.protocol.command.debug;

import com.vr.cdp.protocol.command.response.EmptyResult;

public class DebuggerResume extends DebugCommand<EmptyResult> {

    public DebuggerResume() {
        super("Debugger.resume");
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
