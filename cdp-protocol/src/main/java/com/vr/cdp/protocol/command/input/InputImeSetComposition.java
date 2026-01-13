package com.vr.cdp.protocol.command.input;


import com.vr.cdp.protocol.command.response.EmptyResult;

public class InputImeSetComposition extends InputCommand<EmptyResult> {

    private final Params params;

    public InputImeSetComposition(String text) {
        super("Input.imeSetComposition");
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

    public record Params(String text) {}
}
