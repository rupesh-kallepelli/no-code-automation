package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class RuntimeEvaluate
        extends RuntimeCommand<RuntimeEvaluate.Result> {

    private final Params params;

    public RuntimeEvaluate(String expression) {
        super("Runtime.evaluate");
        this.params = new Params(expression, true);
    }

    public RuntimeEvaluate(String expression, boolean returnByValue) {
        super("Runtime.evaluate");
        this.params = new Params(expression, returnByValue);
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
            String expression,
            boolean returnByValue
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(RemoteObject result) {}
}
