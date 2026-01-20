package com.vr.actions.element;

import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMFocus;
import com.vr.cdp.protocol.command.dom.DOMGetBoxModel;
import com.vr.cdp.protocol.command.input.InputDispatchMouseEvent;
import com.vr.cdp.protocol.command.input.InputInsertText;

import java.util.List;

public class ElementHandle {

    private final CDPClient client;
    private final int nodeId;

    public ElementHandle(CDPClient client, int nodeId) {
        this.client = client;
        this.nodeId = nodeId;
    }

    public void focus() throws Exception {
        client.send(new DOMFocus(nodeId));
    }

    public void type(String text) throws Exception {
        focus();
        client.send(new InputInsertText(text));
    }

    public void click() throws Exception {
        DOMGetBoxModel.Result box = client.sendAndWait(new DOMGetBoxModel(nodeId));

        List<Double> c = box.model().content();
        double x = (c.get(0) + c.get(4)) / 2;
        double y = (c.get(1) + c.get(5)) / 2;

        client.send(new InputDispatchMouseEvent(
                "mousePressed", x, y, "left", 1
        ));
        client.send(new InputDispatchMouseEvent(
                "mouseReleased", x, y, "left", 1
        ));
    }
}
