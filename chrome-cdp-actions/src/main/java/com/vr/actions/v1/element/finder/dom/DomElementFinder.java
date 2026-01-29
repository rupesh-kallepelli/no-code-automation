package com.vr.actions.v1.element.finder.dom;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.AbstractElementFiner;
import com.vr.actions.v1.element.finder.dom.exception.DOMNotFoundException;
import com.vr.actions.v1.element.finder.dom.exception.ElementNotFoundException;
import com.vr.actions.v1.element.impl.ElementImpl;
import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMEnable;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.dom.DOMQuerySelector;
import com.vr.cdp.protocol.command.page.PageGetFrameTree;

import javax.lang.model.util.Elements;
import java.util.List;
import java.util.Objects;

public class DomElementFinder extends AbstractElementFiner {

    public DomElementFinder(CDPClient client) {
        super(client);
        try {
            client.sendAndWait(new DOMEnable());
            rootNode = getRootNode();
        } catch (Exception e) {
            throw new DOMNotFoundException(e.getMessage());
        }
    }

    @Override
    public Element findElement(Selector selector) {
        try {
            DOMQuerySelector.Result querySelectorResult = this.client.sendAndWait(new DOMQuerySelector(getRootNode(), selector.getSelectorValue()));

            if (querySelectorResult.nodeId() == null || querySelectorResult.nodeId() == 0) {
                List<PageGetFrameTree.Frame> frames = getFlattenedFrames();

                for (PageGetFrameTree.Frame frame : frames) {

                    Integer rootNodeId = getFrameDocumentNodeId(frame.id());

                    if (rootNodeId == null) {
                        continue;
                    }

                    querySelectorResult = this.client.sendAndWait(
                            new DOMQuerySelector(rootNodeId, selector.getSelectorValue())
                    );

                    if (querySelectorResult.nodeId() != null &&
                            querySelectorResult.nodeId() != 0) {
                        break;
                    }
                }
            }

            if (Objects.isNull(querySelectorResult.nodeId()) || Objects.equals(querySelectorResult.nodeId(), 0)) {
                throw new ElementNotFoundException("Element not found with selector : " + selector.getSelectorValue());
            }

            return new ElementImpl(
                    new com.vr.cdp.actions.v1.element.Element.Node(
                            querySelectorResult.nodeId(),
                            selector,
                            Element.IdentifiedBy.DOM
                    ),
                    client
            );
        } catch (Exception e) {
            throw new ElementNotFoundException("Couldn't find the element with css selector : " + selector.getSelectorValue(), e);
        }
    }

    private Integer getFrameDocumentNodeId(String frameId) throws Exception {
        DOMGetDocument.Result doc = this.client.sendAndWait(new DOMGetDocument());
        return findFrameDocumentNodeId(doc.root(), frameId);
    }

    private Integer findFrameDocumentNodeId(DOMGetDocument.Node node, String frameId) {

        if ("IFRAME".equals(node.nodeName())
                && frameId.equals(node.frameId())
                && node.contentDocument() != null) {

            return node.contentDocument().nodeId();
        }

        if (node.children() != null) {
            for (DOMGetDocument.Node child : node.children()) {
                Integer result = findFrameDocumentNodeId(child, frameId);
                if (result != null) return result;
            }
        }

        return null;
    }


    @Override
    public List<Elements> findElements(Selector selector) {
        throw new RuntimeException("No implementation yet");
    }

}
