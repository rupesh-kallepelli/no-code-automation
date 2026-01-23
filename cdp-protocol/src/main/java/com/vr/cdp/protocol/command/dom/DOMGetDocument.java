package com.vr.cdp.protocol.command.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class DOMGetDocument
        extends DOMCommand<DOMGetDocument.Result> {

    private final Params params;

    public DOMGetDocument(int depth) {
        super("DOM.getDocument");
        this.params = new Params(null, depth, true);
    }

    public DOMGetDocument(String frameId, int depth) {
        super("DOM.getDocument");
        this.params = new Params(frameId, depth, true);
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Params(String frameId, int depth, boolean pierce) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(Node root) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Node(
            Integer nodeId,
            Integer parentId,
            Integer backendNodeId,
            Integer nodeType,
            String nodeName,
            String localName,
            String nodeValue,
            Integer childNodeCount,
            List<Node> children,
            List<String> attributes,
            String documentURL,
            String baseURL,
            String publicId,
            String systemId,
            String internalSubset,
            String xmlVersion,
            String name,
            String value,
            String pseudoType,
            String pseudoIdentifier,
            String shadowRootType,
            String frameId,
            Node contentDocument,
            List<Node> shadowRoots,
            Node templateContent,
            List<Node> pseudoElements,
            Node importedDocument,
            List<Integer> distributedNodes,
            Boolean isSVG,
            String compatibilityMode,
            Integer assignedSlot,
            Boolean isScrollable,
            Boolean affectedByStartingStyles,
            List<String> adoptedStyleSheets
    ) {}

}
