package com.vr.actions.v1.element.finder;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.dom.DomElementFinder;
import com.vr.actions.v1.element.finder.dom.exception.ElementNotFoundException;
import com.vr.actions.v1.element.finder.runtime.RuntimeElementFinder;
import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;

public class ElementResolver {
    private final CDPClient client;

    public ElementResolver(CDPClient client) {
        this.client = client;
    }

    private Element resolve(Selector selector) {
        return switch (selector.getSelectorType()) {
            case CSS -> new DomElementFinder(client).findElement(selector);
            case CLASS ->
                    new DomElementFinder(client).findElement(Selector.selectByCssSelector(".%s".formatted(selector.getSelectorValue())));
            case ID ->
                    new DomElementFinder(client).findElement(Selector.selectByCssSelector("#%s".formatted(selector.getSelectorValue())));
            case TAG ->
                    new DomElementFinder(client).findElement(Selector.selectByCssSelector("%s".formatted(selector.getSelectorValue())));
            case XPATH -> new RuntimeElementFinder(client).findElement(selector);
            case TEXT ->
                    new RuntimeElementFinder(client).findElement(Selector.selectByXPath("//*[text()='%s']".formatted(selector.getSelectorValue())));
            case LINK_TEXT ->
                    new RuntimeElementFinder(client).findElement(Selector.selectByXPath("//a[text()='%s']".formatted(selector.getSelectorValue())));
        };
    }


    public Element resolveElement(Selector selector) {
        return resolve(selector);
    }

    public Element resolveElementWithTimeout(Selector selector, long timeoutMs) {
        long start = System.currentTimeMillis();
        Element e = null;
        while (System.currentTimeMillis() - start < timeoutMs) {
            try {
                e = resolveElement(selector); // existing logic
                if (e != null) break;
                Thread.sleep(100);
            } catch (ElementNotFoundException ignored) {
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
        }
        if (e == null)
            throw new ElementNotFoundException("Element not found in time with selector : " + selector.getSelectorValue());
        return e;
    }

}
