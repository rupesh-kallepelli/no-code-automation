package com.vr.cdp.protocol.command.debug;

import com.vr.cdp.protocol.command.CDPCommand;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class DebuggerSetPauseOnExceptions
        extends DebugCommand<EmptyResult> {

    private final Params params;

    public DebuggerSetPauseOnExceptions(String state) {
        super("Debugger.setPauseOnExceptions");
        this.params = new Params(state);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(String state) {
        // "none", "uncaught", "all"
    }
}
