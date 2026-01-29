package com.vr.cdp.actions.v1.element.actions.type;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.client.CDPClient;

public interface Typeable {
    void type(Element.Node node, CDPClient client, CharSequence charSequence);

    void typeIndividualChar(Element.Node node, CDPClient client, CharSequence charSequence);

    void typeIndividualChar(Element.Node node, CDPClient client, CharSequence charSequence, long millis);

}
