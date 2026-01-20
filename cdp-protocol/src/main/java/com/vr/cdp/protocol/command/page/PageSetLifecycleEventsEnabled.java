package com.vr.cdp.protocol.command.page;

import com.vr.cdp.protocol.command.CDPCommand;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class PageSetLifecycleEventsEnabled
        extends PageCommand<EmptyResult> {

    private final Params params;

    public PageSetLifecycleEventsEnabled(boolean enabled) {
        super("Page.setLifecycleEventsEnabled");
        this.params = new Params(enabled);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(boolean enabled) {}
}
