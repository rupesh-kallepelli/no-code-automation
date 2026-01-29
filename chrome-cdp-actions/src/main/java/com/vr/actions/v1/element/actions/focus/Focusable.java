package com.vr.actions.v1.element.actions.focus;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.focus.exception.UnableToFocusException;
import com.vr.cdp.actions.v1.element.actions.focus.Focus;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeEvaluate;

public interface Focusable extends com.vr.cdp.actions.v1.element.actions.focus.Focusable, DOMFocus, RuntimeFocus {
    default Focus focus(Element.Node node, CDPClient client) {
        return switch (node.getIdentifiedBy()) {
            case DOM -> DOMFocus.super.focus(node, client);
            case RUNTIME -> RuntimeFocus.super.focus(node, client);
            case BOTH -> null;
        };
    }

    default void blurActive(CDPClient client) {
        try {
            client.sendAndWait(new RuntimeEvaluate(
                    "document.activeElement && document.activeElement.blur()"
            ));
        } catch (Exception e) {
            throw new UnableToFocusException("Unable to blur", e);
        }
    }

}
