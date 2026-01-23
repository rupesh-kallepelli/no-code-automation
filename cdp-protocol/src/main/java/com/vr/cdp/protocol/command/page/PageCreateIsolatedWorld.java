package com.vr.cdp.protocol.command.page;

public class PageCreateIsolatedWorld
        extends PageCommand<PageCreateIsolatedWorld.Result> {

    private final Params params;

    public PageCreateIsolatedWorld(
            String frameId,
            String worldName,
            boolean grantUniversalAccess
    ) {
        super("Page.createIsolatedWorld");
        this.params = new Params(frameId, worldName, grantUniversalAccess);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    // ---------- PARAMS ----------

    public record Params(
            String frameId,
            String worldName,
            boolean grantUniversalAccess
    ) {}

    // ---------- RESULT ----------

    public record Result(
            int executionContextId
    ) {}
}
