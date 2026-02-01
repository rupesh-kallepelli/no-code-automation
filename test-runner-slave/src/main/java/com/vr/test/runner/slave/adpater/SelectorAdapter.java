package com.vr.test.runner.slave.adpater;


import com.vr.cdp.actions.v1.element.selector.Selector;

public class SelectorAdapter {
    public static Selector adaptToElementSelector(com.vr.test.runner.slave.request.Selector selector) {
        return switch (selector.selectorType()) {
            case CSS -> Selector.selectByCssSelector(selector.selectorValue());
            case XPATH -> Selector.selectByXPath(selector.selectorValue());
            case TEXT -> Selector.selectByText(selector.selectorValue());
            case CLASS -> Selector.selectByClass(selector.selectorValue());
            case TAG -> Selector.selectByTag(selector.selectorValue());
        };
    }
}
