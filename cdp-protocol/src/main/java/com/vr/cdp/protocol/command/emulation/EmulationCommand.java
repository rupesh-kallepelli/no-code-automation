package com.vr.cdp.protocol.command.emulation;

import com.vr.cdp.protocol.command.CDPCommand;

public abstract class EmulationCommand<R> extends CDPCommand<R> {

    protected EmulationCommand(String method) {
        super(method);
    }
}
