package com.vr.cdp.actions.v1.element.actions.click;


import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface Clickable {

    void click(Element.Node node, CDPClient client);

    void rightClick(Element.Node node, CDPClient client);
}
