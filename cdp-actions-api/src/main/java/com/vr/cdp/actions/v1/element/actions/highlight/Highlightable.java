package com.vr.cdp.actions.v1.element.actions.highlight;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface Highlightable {
    void highlight(Element.Node node, CDPClient client);

    void hideHighlight(CDPClient client);
}
