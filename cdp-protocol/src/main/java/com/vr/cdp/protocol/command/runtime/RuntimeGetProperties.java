package com.vr.cdp.protocol.command.runtime;

import com.vr.cdp.protocol.command.CDPCommand;

import java.util.List;

public class RuntimeGetProperties extends CDPCommand<RuntimeGetProperties.Result> {

    private final Params params;

    public RuntimeGetProperties(String objectId) {
        super("Runtime.getProperties");
        this.params = new Params(objectId, true, false);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    /* ---------- Records ---------- */

    public record Params(
            String objectId,
            Boolean ownProperties,
            Boolean accessorPropertiesOnly
    ) {}

    public record Result(
            List<Object> result,
            List<Object> internalProperties
    ) {
    }

    public record PropertyDescriptor(
            String name,
            RemoteObject value,
            Boolean writable,
            Boolean configurable,
            Boolean enumerable,
            Boolean isOwn
    ) {
    }

    public record InternalPropertyDescriptor(
            String name,
            RemoteObject value
    ) {}

}
