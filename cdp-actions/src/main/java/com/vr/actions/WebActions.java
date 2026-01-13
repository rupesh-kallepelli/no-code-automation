package com.vr.actions;


import com.vr.actions.dom.DOMActions;
import com.vr.actions.input.InputActions;

public class WebActions {

    private final DOMActions dom;
    private final InputActions input;

    public WebActions(ActionContext ctx) {
        this.dom = new DOMActions(ctx);
        this.input = new InputActions(ctx);
    }

    public void click(String selector) throws Exception {
        int nodeId = dom.find(selector);
        input.click(nodeId);
    }

    public void type(String selector, String text) throws Exception {
        int nodeId = dom.find(selector);
        dom.focus(nodeId);
        input.type(text);
    }
}
