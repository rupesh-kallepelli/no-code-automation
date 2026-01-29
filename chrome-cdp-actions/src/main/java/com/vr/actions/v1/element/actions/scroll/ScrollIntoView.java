package com.vr.actions.v1.element.actions.scroll;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.scroll.exception.ScrollException;
import com.vr.cdp.client.CDPClient;

public interface ScrollIntoView extends com.vr.cdp.actions.v1.element.actions.scroll.ScrollIntoView {
    default void scrollIntoView(Element.Node node, CDPClient client) {
        try {
            switch (node.getIdentifiedBy()) {
                case DOM -> DOMScrollIntoView.scrollIntoView(client, node);
                case RUNTIME -> RuntimeScrollIntoView.scrollIntoView(client, node);
                case BOTH -> throw new Exception("Scroll is not implemented for both");
            }
        } catch (Exception e) {
            throw new ScrollException("Couldn't scroll to element", e);
        }
    }
}
