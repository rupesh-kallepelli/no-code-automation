package com.vr.cdp.protocol.command.overlay;

import com.vr.cdp.protocol.command.CDPCommand;

public abstract class OverlayCommand<R> extends CDPCommand<R> {

    protected OverlayCommand(String method) {
        super(method);
    }
}