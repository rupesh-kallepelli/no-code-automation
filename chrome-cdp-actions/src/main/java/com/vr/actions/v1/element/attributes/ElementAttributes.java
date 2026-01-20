package com.vr.actions.v1.element.attributes;


import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.attributes.exception.UnableToGetAttributesException;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMGetAttributes;
import com.vr.cdp.protocol.command.dom.DOMResolveNode;
import com.vr.cdp.protocol.command.runtime.RuntimeGetProperties;

import java.util.List;

public interface ElementAttributes {
    default List<String> getAttributes(Element.Node node, CDPClient client) {
        try {
            DOMGetAttributes.Result result = client.sendAndWait(new DOMGetAttributes(node.getNodeId()));
            return result.attributes();
        } catch (Exception e) {
            throw new UnableToGetAttributesException("Unable to get the attributes", e);
        }
    }

    default RuntimeGetProperties.Result getProperties(Element.Node node, CDPClient client) {
        try {
            DOMResolveNode.Result domResolveNodeResult = client.sendAndWait(new DOMResolveNode(node.getNodeId(), false));
            return client.sendAndWait(new RuntimeGetProperties(domResolveNodeResult.object().objectId()));
        } catch (Exception e) {
            throw new UnableToGetAttributesException("Unable to get the properties", e);
        }
    }

}
