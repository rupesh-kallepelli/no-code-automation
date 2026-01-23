package com.vr.cdp.protocol.command.input;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class InputDispatchMouseEvent extends InputCommand<EmptyResult> {

    private final Params params;

    public InputDispatchMouseEvent(
            String type,
            Number x,
            Number y,
            String button,
            int clickCount
    ) {
        super("Input.dispatchMouseEvent");
        this.params = new Params(type, x, y, button, clickCount);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Params(
            String type,     // mousePressed, mouseReleased, mouseMoved, mouseWheel
            Number x,
            Number y,
            String button,   // left, right, middle, none
            int clickCount
    ) {}
}
