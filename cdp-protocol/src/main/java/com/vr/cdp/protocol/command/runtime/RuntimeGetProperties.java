package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class RuntimeGetProperties
        extends RuntimeCommand<RuntimeGetProperties.Result> {

    private final Params params;

    public RuntimeGetProperties(String objectId) {
        super("Runtime.getProperties");
        this.params = new Params(objectId, true);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    public record Params(
            String objectId,
            boolean ownProperties
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(List<PropertyDescriptor> result) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PropertyDescriptor(
            String name,
            RemoteObject value
    ) {}
}
