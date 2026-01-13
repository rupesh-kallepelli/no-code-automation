package com.vr.cdp.protocol.command.fetch;

import com.vr.cdp.protocol.command.CDPCommand;

public abstract class FetchCommand<R> extends CDPCommand<R> {

    protected FetchCommand(String method) {
        super(method);
    }
}
