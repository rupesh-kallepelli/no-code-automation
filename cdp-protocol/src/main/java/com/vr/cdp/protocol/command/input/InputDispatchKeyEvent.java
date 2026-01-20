package com.vr.cdp.protocol.command.input;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.vr.cdp.protocol.command.response.EmptyResult;

public class InputDispatchKeyEvent extends InputCommand<EmptyResult> {

    private final Params params;

    public InputDispatchKeyEvent(
            String type,
            String text
//            String code,
//            int keyCode,
//            boolean ctrl,
//            boolean alt,
//            boolean shift,
//            boolean meta
    ) {
        super("Input.dispatchKeyEvent");
        this.params = new Params(
                type, text
//                , code, keyCode,
//                ctrl, alt, shift, meta
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Params(
            String type,      // keyDown, keyUp, char
            String text
//            String code,
//            int windowsVirtualKeyCode,
//            boolean ctrlKey,
//            boolean altKey,
//            boolean shiftKey,
//            boolean metaKey
    ) {}
}
