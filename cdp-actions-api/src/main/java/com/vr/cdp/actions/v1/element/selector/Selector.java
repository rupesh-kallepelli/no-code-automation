package com.vr.cdp.actions.v1.element.selector;

import java.util.Objects;

public class Selector {
    private final SelectorType selectorType;
    private final String selectorValue;

    public SelectorType getSelectorType() {
        return selectorType;
    }

    public String getSelectorValue() {
        return selectorValue;
    }

    private Selector(String selectorValue, SelectorType selectorType) {
        this.selectorType = selectorType;
        this.selectorValue = selectorValue;
    }

    public static Selector selectByXPath(String xpath) {
        return new Selector(xpath, SelectorType.XPATH);
    }

    public static Selector selectByCssSelector(String cssSelector) {
        return new Selector(cssSelector, SelectorType.CSS);
    }

    public static Selector selectByText(String text) {
        return new Selector(text, SelectorType.TEXT);
    }

    public static Selector selectByTag(String tagName) {
        return new Selector(tagName, SelectorType.TAG);
    }

    public static Selector selectByLinkText(String linkText) {
        return new Selector(linkText, SelectorType.LINK_TEXT);
    }

    public static Selector selectByID(String link) {
        return new Selector(link, SelectorType.ID);
    }

    public static Selector selectByClass(String className) {
        return new Selector(className, SelectorType.CLASS);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Selector selector = (Selector) o;
        return selectorType == selector.selectorType && Objects.equals(selectorValue, selector.selectorValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectorType, selectorValue);
    }
}
