package com.vr.cdp.protocol.command.runtime;


import com.vr.cdp.protocol.command.CDPCommand;

public abstract class RuntimeCommand<R> extends CDPCommand<R> {

    protected RuntimeCommand(String method) {
        super(method);
    }
}
