package com.vr.cdp.protocol.command.page;

import com.vr.cdp.protocol.command.params.EmptyParams;

public class PageGetFrameTree
        extends PageCommand<PageGetFrameTree.Result> {

    public PageGetFrameTree() {
        super("Page.getFrameTree");
    }

    @Override
    public Object getParams() {
        return EmptyParams.INSTANCE;
    }


    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Result(Object frameTree) {}
}
