package com.vr.cdp.protocol.command.target;

import com.vr.cdp.protocol.command.CDPCommand;

public abstract class TargetCommand<R> extends CDPCommand<R> {

    protected TargetCommand(String method) {
        super(method);
    }
}
