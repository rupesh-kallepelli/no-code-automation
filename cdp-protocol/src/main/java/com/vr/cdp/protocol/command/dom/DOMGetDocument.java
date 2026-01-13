package com.vr.cdp.protocol.command.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class DOMGetDocument
        extends DOMCommand<DOMGetDocument.Result> {

    private final Params params;

    public DOMGetDocument(int depth) {
        super("DOM.getDocument");
        this.params = new Params(depth);
    }

    public DOMGetDocument() {
        this(-1);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(int depth) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(Node root) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Node(
            int nodeId,
            String nodeName,
            String localName,
            String nodeValue
    ) {}
}
