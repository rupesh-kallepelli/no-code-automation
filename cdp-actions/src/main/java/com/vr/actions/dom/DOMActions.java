package com.vr.actions.dom;


import com.vr.actions.ActionContext;
import com.vr.cdp.protocol.command.dom.DOMFocus;
import com.vr.cdp.protocol.command.dom.DOMGetDocument;
import com.vr.cdp.protocol.command.dom.DOMQuerySelector;

public class DOMActions {

    private final ActionContext ctx;
    private Integer cachedRoot;

    public DOMActions(ActionContext ctx) {
        this.ctx = ctx;
    }

    public int root() throws Exception {
        return ctx.client().sendAndWait(
                new DOMGetDocument(-1)
        ).root().nodeId();
    }

    public boolean exists(String selector) {
        try {
            int root = root();
            int nodeId = ctx.client().sendAndWait(
                    new DOMQuerySelector(root, selector)
            ).nodeId();
            return nodeId != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int find(String selector) throws Exception {
        // ⏳ WAIT until element exists in DOM
        com.vr.actions.wait.Wait.until(
                () -> exists(selector),
                10_000,
                200
        );
        // 🔍 Resolve element AFTER it exists
        int root = root();
        int nodeId = ctx.client().sendAndWait(
                new DOMQuerySelector(root, selector)
        ).nodeId();

        if (nodeId == 0) {
            throw new RuntimeException("Element not found: " + selector);
        }

        return nodeId;
    }

    public void focus(int nodeId) throws Exception {
        ctx.client().send(new DOMFocus(nodeId));
    }
}
