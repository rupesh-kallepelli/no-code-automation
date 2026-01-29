package com.vr.actions.v1.element.finder.runtime;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.AbstractElementFiner;
import com.vr.actions.v1.element.finder.dom.exception.DOMNotFoundException;
import com.vr.actions.v1.element.finder.dom.exception.ElementNotFoundException;
import com.vr.actions.v1.element.impl.ElementImpl;
import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMEnable;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.dom.DOMRequestNode;
import com.vr.cdp.protocol.command.page.PageCreateIsolatedWorld;
import com.vr.cdp.protocol.command.page.PageGetFrameTree;
import com.vr.cdp.protocol.command.runtime.RuntimeEnable;
import com.vr.cdp.protocol.command.runtime.RuntimeEvaluate;

import javax.lang.model.util.Elements;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeElementFinder extends AbstractElementFiner {

    private final Map<String, Integer> frameContexts = new ConcurrentHashMap<>();

    public RuntimeElementFinder(CDPClient client) {
        super(client);
        try {
            client.sendAndWait(new RuntimeEnable());
            client.sendAndWait(new DOMEnable());
            client.sendAndWait(new DOMGetDocument());
        } catch (Exception e) {
            throw new DOMNotFoundException(e.getMessage());
        }

    }

    @Override
    public Element findElement(Selector selector) {
        try {
            DOMRequestNode.Result nodeId = null;

            String jsonQuery = getXpathJsExpression(selector.getSelectorValue());
            RuntimeEvaluate.Result runtimeResult = this.client.sendAndWait(new RuntimeEvaluate(jsonQuery, false));

            String objectId = runtimeResult.result().objectId();

            if (Objects.nonNull(objectId))
                nodeId = client.sendAndWait(new DOMRequestNode(objectId));

            if (runtimeResult.result().objectId() == null || (Objects.isNull(nodeId) || nodeId.nodeId() == 0)) {
                List<PageGetFrameTree.Frame> frames = getFlattenedFrames();

                for (PageGetFrameTree.Frame frame : frames) {

                    int contextId = getOrCreateContext(frame.id());

                    runtimeResult = this.client.sendAndWait(new RuntimeEvaluate(contextId, jsonQuery, false));
                    objectId = runtimeResult.result().objectId();

                    if (Objects.nonNull(objectId)) {
                        nodeId = client.sendAndWait(new DOMRequestNode(objectId));
                        if (nodeId.nodeId() != 0)
                            break;
                    }
                }
            }

            if (runtimeResult.result().objectId() == null || (Objects.isNull(nodeId) || nodeId.nodeId() == 0))
                throw new ElementNotFoundException("Could not find the element : " + selector.getSelectorValue());


            return new ElementImpl(
                    new Element.Node(
                            nodeId.nodeId(),
                            selector,
                            Element.IdentifiedBy.DOM
                    ),
                    client
            );

        } catch (Exception e) {
            throw new ElementNotFoundException("Couldn't find the element with xpath selector : " + selector.getSelectorValue(), e);
        }
    }

    private int getOrCreateContext(String frameId) {
        return frameContexts.computeIfAbsent(frameId, id -> {
            try {
                return client.sendAndWait(
                        new PageCreateIsolatedWorld(id, "automation-context", true)
                ).executionContextId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getXpathJsExpression(String selectorValue) {
        return """
                document.evaluate(
                        "<xpath>",
                        document,
                        null,
                        XPathResult.FIRST_ORDERED_NODE_TYPE,
                        null
                    )
                .singleNodeValue""".replace("<xpath>", selectorValue);
    }

    @Override
    public List<Elements> findElements(Selector selector) {
        throw new RuntimeException("No implementation yet");
    }
}
