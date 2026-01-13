package com.vr.cdp.protocol.command.input;


import com.vr.cdp.protocol.command.CDPCommand;

public abstract class InputCommand<R> extends CDPCommand<R> {

    protected InputCommand(String method) {
        super(method);
    }
}
