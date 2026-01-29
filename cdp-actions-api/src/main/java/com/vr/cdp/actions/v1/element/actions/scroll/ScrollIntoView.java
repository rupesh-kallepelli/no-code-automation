package com.vr.cdp.actions.v1.element.actions.scroll;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface ScrollIntoView {
    void scrollIntoView(Element.Node node, CDPClient client);
}
