package com.vr.cdp.runtime;

import com.vr.cdp.client.CDPClient;

public abstract class BrowserClient {

    protected final CDPClient client;

    protected BrowserClient(CDPClient client) {
        this.client = client;
    }
}
