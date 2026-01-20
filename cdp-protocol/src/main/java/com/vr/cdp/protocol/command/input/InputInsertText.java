package com.vr.cdp.protocol.command.input;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class InputInsertText extends InputCommand<EmptyResult> {

    private final Params params;

    public InputInsertText(CharSequence text) {
        super("Input.insertText");
        this.params = new Params(text);
    }

    @Override
    public Object getParams() {
        return params;
    }

    @Override
    public Class<EmptyResult> getResultType() {
        return EmptyResult.class;
    }

    public record Params(CharSequence text) {
    }
}
