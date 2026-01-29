package com.vr.actions.v1.element.actions.highlight;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.highlight.exception.UnableToHighlightElementException;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.overlay.OverlayEnable;
import com.vr.cdp.protocol.command.overlay.OverlayHideHighlight;
import com.vr.cdp.protocol.command.overlay.OverlayHighlightNode;

public interface Highlightable extends com.vr.cdp.actions.v1.element.actions.highlight.Highlightable {
    default void highlight(Element.Node node, CDPClient client) {
        try {
            client.sendAndWait(new OverlayEnable());
            client.sendAndWait(new OverlayHighlightNode(node.getNodeId()));
        } catch (Exception e) {
            throw new UnableToHighlightElementException("Unable to highlight the element", e);
        }
    }

    default void hideHighlight(CDPClient client) {
        try {
            client.sendAndWait(new OverlayEnable());
            client.sendAndWait(new OverlayHideHighlight());
        } catch (Exception e) {
            throw new UnableToHighlightElementException("Unable to highlight the element", e);
        }
    }
}
