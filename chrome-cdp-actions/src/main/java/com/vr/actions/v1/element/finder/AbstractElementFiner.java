package com.vr.actions.v1.element.finder;

import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.dom.DOMEnable;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;

public abstract class AbstractElementFiner implements ElementFinder {
    protected final CDPClient client;
    protected int rootNode = -1;

    protected AbstractElementFiner(CDPClient client) {
        this.client = client;
    }

    protected int getRootNode() throws Exception {
        this.client.sendAndWait(new DOMEnable());
        return this.client.sendAndWait(new DOMGetDocument(-1)).root().nodeId();
    }


}
