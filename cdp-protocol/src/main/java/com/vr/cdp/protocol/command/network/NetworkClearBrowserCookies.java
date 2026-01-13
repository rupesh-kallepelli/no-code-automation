package com.vr.cdp.protocol.command.network;


import com.vr.cdp.protocol.command.params.EmptyParams;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class NetworkClearBrowserCookies
        extends NetworkCommand<EmptyResult> {

    public NetworkClearBrowserCookies() {
        super("Network.clearBrowserCookies");
    }

    @Override
    public Object getParams() {
        return EmptyParams.INSTANCE;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }
}
