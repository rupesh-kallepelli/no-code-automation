package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

public class RuntimeEvaluate
        extends RuntimeCommand<RuntimeEvaluate.Result> {

    private final Params params;

    public RuntimeEvaluate(String expression) {
        super("Runtime.evaluate");
        this.params = new Params(null, expression, true);
    }

    public RuntimeEvaluate(String expression, boolean returnByValue) {
        super("Runtime.evaluate");
        this.params = new Params(null, expression, returnByValue);
    }

    public RuntimeEvaluate(int contextId, String expression, boolean returnByValue) {
        super("Runtime.evaluate");
        this.params = new Params(contextId, expression, returnByValue);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<Result> getResultType() {
        return Result.class;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Params(
            Integer contextId,
            String expression,
            boolean returnByValue
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(RemoteObject result) {}
}
