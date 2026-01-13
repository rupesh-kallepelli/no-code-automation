package com.vr.cdp.protocol.command.page;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vr.cdp.protocol.command.params.EmptyParams;

import java.util.List;

public class PageGetNavigationHistory
        extends PageCommand<PageGetNavigationHistory.Result> {

    public PageGetNavigationHistory() {
        super("Page.getNavigationHistory");
    }

    @Override
    public Object getParams() {
        return EmptyParams.INSTANCE;   // ✅ FIX
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            int currentIndex,
            List<Entry> entries
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Entry(
            int id,
            String url,
            String title
    ) {
    }
}
