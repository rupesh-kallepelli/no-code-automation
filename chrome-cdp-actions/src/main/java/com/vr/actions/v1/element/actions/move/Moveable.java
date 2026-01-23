package com.vr.actions.v1.element.actions.move;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.focus.DOMFocus;
import com.vr.actions.v1.element.actions.focus.Focus;
import com.vr.actions.v1.element.actions.move.exception.UnableToMoveException;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMGetBoxModel;
import com.vr.cdp.protocol.command.input.InputDispatchMouseEvent;

import java.util.List;

public interface Moveable extends DOMFocus {
    default void moveToElement(Element.Node node, CDPClient client) {
        try {
            Focus focus = focus(node, client);
            client.sendAndWait(new InputDispatchMouseEvent(
                    "mouseMoved",
                    focus.centerX(),
                    focus.centerY(),
                    null,
                    0));
        } catch (Exception e) {
            throw new UnableToMoveException("Unable to move to target element", e);
        }
    }

    default void dragAndDrop(Element.Node target, Element.Node destination, CDPClient client) {
        try {
            Focus targetFocus = focus(target, client);
            client.send(new InputDispatchMouseEvent(
                    "mousePressed", targetFocus.centerX(), targetFocus.centerY(), "left", 1
            ));
            client.send(new com.vr.cdp.protocol.command.dom.DOMFocus(destination.getNodeId()));
            DOMGetBoxModel.Result box = client.sendAndWait(new DOMGetBoxModel(destination.getNodeId()));
            List<Double> c = box.model().content();
            //center
            double centerX = (c.get(0) + c.get(4)) / 2;
            double centerY = (c.get(1) + c.get(5)) / 2;
            //height and width
            double x = c.get(0);
            double y = c.get(1);
            client.sendAndWait(new InputDispatchMouseEvent(
                    "mouseMoved",
                    centerX,
                    centerY,
                    null,
                    0));
            client.send(new InputDispatchMouseEvent(
                    "mouseReleased",  centerX,
                    centerY, "left", 1
            ));
        } catch (Exception e) {
            throw new UnableToMoveException("Unable to move to target element", e);
        }
    }
}
