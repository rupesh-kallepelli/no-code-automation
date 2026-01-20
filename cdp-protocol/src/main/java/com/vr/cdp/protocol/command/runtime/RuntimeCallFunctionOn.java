package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class RuntimeCallFunctionOn
        extends RuntimeCommand<RuntimeCallFunctionOn.Result> {

    private final Params params;

    public RuntimeCallFunctionOn(
            String functionDeclaration,
            String objectId,
            List<CallArgument> arguments
    ) {
        super("Runtime.callFunctionOn");
        this.params = new Params(
                functionDeclaration,
                objectId,
                arguments,
                true
        );
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
            String functionDeclaration,
            String objectId,
            List<CallArgument> arguments,
            boolean returnByValue
    ) {}

    public record CallArgument(
            Object value,
            String objectId
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(RemoteObject result) {}
}
