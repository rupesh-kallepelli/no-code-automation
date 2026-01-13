package com.vr.cdp.runtime.wait;

import com.vr.cdp.runtime.dom.DOMService;

import java.util.function.BooleanSupplier;

public class ElementVisible implements BooleanSupplier {

    private final DOMService dom;
    private final String selector;

    public ElementVisible(DOMService dom, String selector) {
        this.dom = dom;
        this.selector = selector;
    }

    @Override
    public boolean getAsBoolean() {
        try {
            dom.find(selector);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
