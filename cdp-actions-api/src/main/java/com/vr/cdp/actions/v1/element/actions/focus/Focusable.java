package com.vr.cdp.actions.v1.element.actions.focus;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface Focusable {
    Focus focus(Element.Node node, CDPClient client);

    void blurActive(CDPClient client);

}
