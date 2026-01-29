package com.vr.cdp.actions.v1.element.actions.screenshot;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface Screenshot {
    String screenshot(Element.Node node, CDPClient client);
}
