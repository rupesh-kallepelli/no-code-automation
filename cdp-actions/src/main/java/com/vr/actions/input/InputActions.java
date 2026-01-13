package com.vr.actions.input;


import com.vr.actions.ActionContext;
import com.vr.cdp.protocol.command.dom.DOMGetBoxModel;
import com.vr.cdp.protocol.command.input.InputDispatchMouseEvent;
import com.vr.cdp.protocol.command.input.InputInsertText;

import java.util.List;

public class InputActions {

    private final ActionContext ctx;

    public InputActions(ActionContext ctx) {
        this.ctx = ctx;
    }

    public void type(String text) throws Exception {
        ctx.client().send(new InputInsertText(text));
    }

    public void click(int nodeId) throws Exception {
        DOMGetBoxModel.Result box =
                ctx.client().sendAndWait(
                        new DOMGetBoxModel(nodeId)
                );

        List<Double> c = box.model().content();
        double x = (c.get(0) + c.get(4)) / 2;
        double y = (c.get(1) + c.get(5)) / 2;

        ctx.client().send(new InputDispatchMouseEvent(
                "mousePressed", x, y, "left", 1
        ));
        ctx.client().send(new InputDispatchMouseEvent(
                "mouseReleased", x, y, "left", 1
        ));
    }
}
