package com.vr.cdp.protocol.command.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.cdp.protocol.command.CDPCommand;

public class PageNavigate extends CDPCommand<PageNavigate.Result> {

    private final Params params;

    public PageNavigate(String url) {
        super("Page.navigate");
        this.params = new Params(url);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(String url) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            String frameId
    ) {
    }
}

