package com.vr.actions.v1.element.actions.focus;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.scroll.ScrollIntoView;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMGetBoxModel;

import java.util.List;

public interface DOMFocus extends ScrollIntoView {
    default Focus focus(Element.Node node, CDPClient client) throws Exception {
        scrollIntoView(node, client);
        client.send(new com.vr.cdp.protocol.command.dom.DOMFocus(node.getNodeId()));
        DOMGetBoxModel.Result box = client.sendAndWait(new DOMGetBoxModel(node.getNodeId()));
        List<Double> c = box.model().content();
        double x = (c.get(0) + c.get(4)) / 2;
        double y = (c.get(1) + c.get(5)) / 2;
        return new Focus(x, y);
    }

}
