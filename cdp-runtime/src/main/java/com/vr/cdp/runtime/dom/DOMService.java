package com.vr.cdp.runtime.dom;

import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.dom.DOMQuerySelector;

public class DOMService {

    private final CDPClient client;
    private Integer cachedRoot;

    public DOMService(CDPClient client) {
        this.client = client;
    }

    public int root() throws Exception {
        if (cachedRoot == null) {
            cachedRoot = client.sendAndWait(
                    new DOMGetDocument(-1)
            ).root().nodeId();
        }
        return cachedRoot;
    }

    public ElementHandle find(String selector) throws Exception {
        int nodeId = client.sendAndWait(
                new DOMQuerySelector(root(), selector)
        ).nodeId();

        if (nodeId == 0) {
            throw new RuntimeException("Element not found: " + selector);
        }

        return new ElementHandle(client, nodeId);
    }
}
