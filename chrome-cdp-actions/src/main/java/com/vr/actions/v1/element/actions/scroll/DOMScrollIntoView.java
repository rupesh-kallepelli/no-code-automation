package com.vr.actions.v1.element.actions.scroll;

import com.vr.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMScrollIntoViewIfNeeded;

public interface DOMScrollIntoView {
    static void scrollIntoView(CDPClient client, Element.Node node) throws Exception {
        client.sendAndWait(new DOMScrollIntoViewIfNeeded(node.getNodeId()));
    }
}
