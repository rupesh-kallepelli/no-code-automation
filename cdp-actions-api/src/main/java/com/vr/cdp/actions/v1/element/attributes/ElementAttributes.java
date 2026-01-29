package com.vr.cdp.actions.v1.element.attributes;


import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeGetProperties;

import java.util.List;

public interface ElementAttributes {

    List<String> getAttributes(Element.Node node, CDPClient client);

    RuntimeGetProperties.Result getProperties(Element.Node node, CDPClient client);

    String getInnerText(Element.Node node, CDPClient client);

}
