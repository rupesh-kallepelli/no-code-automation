package com.vr.cdp.protocol.command.input;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class InputSetIgnoreInputEvents extends InputCommand<EmptyResult> {

    private final Params params;

    public InputSetIgnoreInputEvents(boolean ignore) {
        super("Input.setIgnoreInputEvents");
        this.params = new Params(ignore);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(boolean ignore) {}
}
