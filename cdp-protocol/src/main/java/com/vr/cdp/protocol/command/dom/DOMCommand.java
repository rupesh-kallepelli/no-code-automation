package com.vr.cdp.protocol.command.dom;


import com.vr.cdp.protocol.command.CDPCommand;

public abstract class DOMCommand<R> extends CDPCommand<R> {

    protected DOMCommand(String method) {
        super(method);
    }
}
