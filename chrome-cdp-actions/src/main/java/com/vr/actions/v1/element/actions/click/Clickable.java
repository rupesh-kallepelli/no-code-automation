package com.vr.actions.v1.element.actions.click;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.click.exception.ClickInterInterceptedExceptionException;
import com.vr.actions.v1.element.actions.focus.Focus;
import com.vr.actions.v1.element.actions.focus.Focusable;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.input.InputDispatchMouseEvent;

public interface Clickable extends Focusable {
    private void mouseClick(Element.Node node, CDPClient client, String mouseButton) {
        try {
            Focus focus = focus(node, client);
            client.send(new InputDispatchMouseEvent(
                    "mousePressed", focus.x(), focus.y(), mouseButton, 1
            ));
            client.send(new InputDispatchMouseEvent(
                    "mouseReleased", focus.x(), focus.y(), mouseButton, 1
            ));
        } catch (Exception e) {
            throw new ClickInterInterceptedExceptionException(e.getMessage());
        }
    }

    default void click(Element.Node node, CDPClient client) {
        mouseClick(node, client, "left");
    }

    default void rightClick(Element.Node node, CDPClient client) {
        mouseClick(node, client, "right");
    }

}
