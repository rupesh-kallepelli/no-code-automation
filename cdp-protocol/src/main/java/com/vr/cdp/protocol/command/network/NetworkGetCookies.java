package com.vr.cdp.protocol.command.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

public class NetworkGetCookies
        extends NetworkCommand<NetworkGetCookies.Result> {

    private final Params params;

    public NetworkGetCookies(List<String> urls) {
        super("Network.getCookies");
        this.params = new Params(urls);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(List<String> urls) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(List<Cookie> cookies) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cookie(
            String name,
            String value,
            String domain,
            String path,
            boolean secure,
            boolean httpOnly
    ) {}
}
