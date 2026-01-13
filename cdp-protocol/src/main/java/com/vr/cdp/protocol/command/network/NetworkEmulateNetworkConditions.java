package com.vr.cdp.protocol.command.network;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class NetworkEmulateNetworkConditions
        extends NetworkCommand<EmptyResult> {

    private final Params params;

    public NetworkEmulateNetworkConditions(
            boolean offline,
            double latency,
            double downloadThroughput,
            double uploadThroughput
    ) {
        super("Network.emulateNetworkConditions");
        this.params = new Params(
                offline, latency,
                downloadThroughput, uploadThroughput
        );
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(
            boolean offline,
            double latency,
            double downloadThroughput,
            double uploadThroughput
    ) {}
}
