package com.vr.cdp.protocol.command.page;


import com.vr.cdp.protocol.command.CDPCommand;

public abstract class PageCommand<R> extends CDPCommand<R> {

    protected PageCommand(String method) {
        super(method);
    }
}
