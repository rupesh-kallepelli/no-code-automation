package com.vr.actions.v1.element.actions.focus;

import com.vr.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeEvaluate;

public interface Focusable extends DOMFocus, RuntimeFocus {
    default Focus focus(Element.Node node, CDPClient client) throws Exception {
        return switch (node.getIdentifiedBy()) {
            case DOM -> DOMFocus.super.focus(node, client);
            case RUNTIME -> RuntimeFocus.super.focus(node, client);
            case BOTH -> null;
        };
    }

    default void blurActive(CDPClient client) throws Exception {
        client.sendAndWait(new RuntimeEvaluate(
                "document.activeElement && document.activeElement.blur()"
        ));
    }

}
