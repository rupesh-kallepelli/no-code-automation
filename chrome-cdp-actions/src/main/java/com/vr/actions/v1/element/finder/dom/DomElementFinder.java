package com.vr.actions.v1.element.finder.dom;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.AbstractElementFiner;
import com.vr.actions.v1.element.finder.dom.exception.DOMNotFoundException;
import com.vr.actions.v1.element.finder.dom.exception.ElementNotFound;
import com.vr.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMEnable;
import com.vr.cdp.protocol.command.dom.DOMQuerySelector;

import javax.lang.model.util.Elements;
import java.util.List;

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
            return new Element.ElementImpl(
                    new Element.Node(
                            querySelectorResult.nodeId(),
                            selector,
                            Element.IdentifiedBy.DOM
                    ),
                    client
            );
        } catch (Exception e) {
            throw new ElementNotFound("Couldn't find the element with css selector : " + selector.getSelectorValue(), e);
        }
    }

//    private Element findElementByCssSelector(Selector selector) {
//        try {
//            DOMQuerySelector.Result querySelectorResult = this.client.sendAndWait(new DOMQuerySelector(getRootNode(), selector.getSelectorValue()));
//            return new Element.ElementImpl(
//                    new Element.Node(
//                            querySelectorResult.nodeId(),
//                            selector,
//                            Element.IdentifiedBy.DOM
//                    ),
//                    client
//            );
//        } catch (Exception e) {
//            throw new ElementNotFound("Couldn't find the element with css selector : " + selector.getSelectorValue(), e);
//        }
//    }

//    private Element findElementByXpathSelector(Selector selector) {
//        try {
//            String jsonQuery = getXpathJsExpression(selector.getSelectorValue());
//            RuntimeEvaluate.Result runtimeResult = this.client.sendAndWait(new RuntimeEvaluate(jsonQuery, false));

    /// /            DOMRequestNode.Result requestNodeResult = this.client.sendAndWait(new DOMRequestNode(runtimeResult.result().objectId()));
    /// /            DOMPerformSearch.Result querySelectorResult = this.client.sendAndWait(new DOMPerformSearch(selector.getSelectorValue()));
    /// /            DOMGetSearchResults.Result searchResult = this.client.sendAndWait(new DOMGetSearchResults(querySelectorResult.searchId(), querySelectorResult.resultCount()));
//            return new Element.ElementImpl(
//                    new Element.Node(
//                            runtimeResult.result().objectId(),
//                            selector,
//                            Element.IdentifiedBy.RUNTIME
//                    ),
//                    client
//            );
//        } catch (Exception e) {
//            throw new ElementNotFound("Couldn't find the element with xpath selector : " + selector.getSelectorValue(), e);
//        }
//    }

//    private String getXpathJsExpression(String selectorValue) {
//        return """
//                document.evaluate(
//                        "<xpath>",
//                        document,
//                        null,
//                        XPathResult.FIRST_ORDERED_NODE_TYPE,
//                        null
//                    )
//                .singleNodeValue""".replace("<xpath>", selectorValue);
//    }
    @Override
    public List<Elements> findElements(Selector selector) {
        throw new RuntimeException("No implementation yet");
    }

}
