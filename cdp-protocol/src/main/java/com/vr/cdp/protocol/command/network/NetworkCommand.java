package com.vr.cdp.protocol.command.network;


import com.vr.cdp.protocol.command.CDPCommand;

public abstract class NetworkCommand<R> extends CDPCommand<R> {

    protected NetworkCommand(String method) {
        super(method);
    }
}