package com.vr.actions.v1.element.finder.runtime;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.AbstractElementFiner;
import com.vr.actions.v1.element.finder.dom.exception.DOMNotFoundException;
import com.vr.actions.v1.element.finder.dom.exception.ElementNotFound;
import com.vr.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMEnable;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.dom.DOMRequestNode;
import com.vr.cdp.protocol.command.runtime.RuntimeEnable;
import com.vr.cdp.protocol.command.runtime.RuntimeEvaluate;

import javax.lang.model.util.Elements;
import java.util.List;

public class RuntimeElementFinder extends AbstractElementFiner {
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
            String jsonQuery = getXpathJsExpression(selector.getSelectorValue());
            RuntimeEvaluate.Result runtimeResult = this.client.sendAndWait(new RuntimeEvaluate(jsonQuery, false));

            DOMRequestNode.Result nodeId = client.sendAndWait(new DOMRequestNode(runtimeResult.result().objectId()));

            return new Element.ElementImpl(
                    new Element.Node(
                            nodeId.nodeId(),
                            selector,
                            Element.IdentifiedBy.DOM
                    ),
                    client
            );

        } catch (Exception e) {
            throw new ElementNotFound("Couldn't find the element with xpath selector : " + selector.getSelectorValue(), e);
        }
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
