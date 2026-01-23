package com.vr.actions.v1.element.finder;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.finder.dom.DomElementFinder;
import com.vr.actions.v1.element.finder.runtime.RuntimeElementFinder;
import com.vr.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.ExecutionContextCreatedEvent;

import java.util.List;

public class ElementResolver {
    private final CDPClient client;

    public ElementResolver(CDPClient client) {
        this.client = client;
    }

    private Element resolve(Selector selector, List<ExecutionContextCreatedEvent> executionContextCreatedEventList) {
        return switch (selector.getSelectorType()) {
            case CSS -> new DomElementFinder(client).findElement(
                    selector,
                    executionContextCreatedEventList
            );
            case CLASS -> new DomElementFinder(client).findElement(
                    Selector.selectByCssSelector(".%s".formatted(selector.getSelectorValue())),
                    executionContextCreatedEventList
            );
            case ID -> new DomElementFinder(client).findElement(
                    Selector.selectByCssSelector("#%s".formatted(selector.getSelectorValue())),
                    executionContextCreatedEventList
            );
            case TAG -> new DomElementFinder(client).findElement(
                    Selector.selectByCssSelector("%s".formatted(selector.getSelectorValue())),
                    executionContextCreatedEventList
            );
            case XPATH -> new RuntimeElementFinder(client).findElement(
                    selector,
                    executionContextCreatedEventList
            );
            case TEXT -> new RuntimeElementFinder(client).findElement(
                    Selector.selectByXPath("//*[text()='%s']".formatted(selector.getSelectorValue())),
                    executionContextCreatedEventList
            );
            case LINK_TEXT -> new RuntimeElementFinder(client).findElement(
                    Selector.selectByXPath("//a[text()='%s']".formatted(selector.getSelectorValue())),
                    executionContextCreatedEventList
            );
        };
    }


    public Element resolveElement(Selector selector, List<ExecutionContextCreatedEvent> executionContextCreatedEventList) {
        return resolve(selector, executionContextCreatedEventList);
    }
}
