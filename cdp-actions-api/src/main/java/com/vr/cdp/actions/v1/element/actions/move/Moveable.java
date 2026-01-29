package com.vr.cdp.actions.v1.element.actions.move;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface Moveable {
    void moveToElement(Element.Node node, CDPClient client);

    void dragAndDrop(Element.Node target, Element.Node destination, CDPClient client);
}
