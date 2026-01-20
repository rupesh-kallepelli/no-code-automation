package com.vr.cdp.protocol.command.debug;

import com.vr.cdp.protocol.command.CDPCommand;

public abstract class DebugCommand<R> extends CDPCommand<R> {
    protected DebugCommand(String method) {
        super(method);
    }
}
