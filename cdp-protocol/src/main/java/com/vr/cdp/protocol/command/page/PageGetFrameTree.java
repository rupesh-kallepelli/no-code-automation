package com.vr.cdp.protocol.command.page;

import com.vr.cdp.protocol.command.params.EmptyParams;

import java.util.List;

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

    // ---------- RESULT ----------

    public record Result(FrameTree frameTree) {}

    // ---------- FRAME TREE ----------

    public record FrameTree(
            Frame frame,
            List<FrameTree> childFrames
    ) {}

    // ---------- FRAME ----------

    public record Frame(
            String id,
            String parentId,
            String loaderId,
            String name,
            String url,
            String domainAndRegistry,
            String securityOrigin,
            SecurityOriginDetails securityOriginDetails,
            String mimeType,
            AdFrameStatus adFrameStatus,
            String secureContextType,
            String crossOriginIsolatedContextType,
            List<String> gatedAPIFeatures
    ) {}

    // ---------- NESTED OBJECTS ----------

    public record SecurityOriginDetails(
            boolean isLocalhost
    ) {}

    public record AdFrameStatus(
            String adFrameType,
            List<String> explanations
    ) {}
}
