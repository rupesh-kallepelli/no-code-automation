package com.vr.actions;

import com.vr.cdp.client.CDPClient;

public class ActionContext {

    private final CDPClient client;

    public ActionContext(CDPClient client) {
        this.client = client;
    }

    public CDPClient client() {
        return client;
    }
}
