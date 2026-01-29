package com.vr.actions.v1.element.impl;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.exception.StaleElementException;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeGetProperties;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;

public class ElementImpl implements Element {
    private final Node node;
    private final CDPClient client;

    public ElementImpl(Node node, CDPClient client) {
        this.node = node;
        this.client = client;
    }

    private <T> T dispatchEvent(Node node, Callable<T> callable) {
        try {
            if (node.getState() == State.STALE)
                throw new StaleElementException("Element you are trying to access is now stale, try create a one");
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatchEvent(Node node, Runnable runnable) {
        if (node.getState() == State.STALE)
            throw new StaleElementException("Element you are trying to access is now stale, try create a one");
        runnable.run();
    }

    @Override
    public void click() {
        dispatchEvent(node, () -> click(node, client));
    }

    @Override
    public void rightClick() {
        dispatchEvent(node, () -> rightClick(node, client));
    }

    @Override
    public void type(CharSequence charSequence) {
        dispatchEvent(node, () -> type(node, client, charSequence));
    }

    @Override
    public void typeIndividualCharacter(CharSequence charSequence) {
        dispatchEvent(node, () -> typeIndividualChar(node, client, charSequence));
    }

    @Override
    public void typeIndividualCharacter(CharSequence charSequence, long millis) {
        dispatchEvent(node, () -> typeIndividualChar(node, client, charSequence, millis));
    }

    @Override
    public void scrollIntoView() {
        dispatchEvent(node, () -> scrollIntoView(node, client));
    }

    @Override
    public void highlight() {
        dispatchEvent(node, () -> highlight(node, client));
    }

    @Override
    public void hideHighlight() {
        dispatchEvent(node, () -> highlight(node, client));
    }

    @Override
    public List<String> getAttributes() {
        return dispatchEvent(node, () -> getAttributes(node, client));
    }

    @Override
    public RuntimeGetProperties.Result getProperties() {
        return dispatchEvent(node, () -> getProperties(node, client));
    }

    @Override
    public void invalidate() {
        node.setState(State.STALE);
    }

    @Override
    public void moveToElement() {
        dispatchEvent(node, () -> moveToElement(node, client));
    }

    @Override
    public void dragToElement(com.vr.cdp.actions.v1.element.Element target) {
        dispatchEvent(node, () -> {
            try {
                dragAndDrop(node,
                        dispatchEvent(node, target::getNode), client);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String getText() {
        return dispatchEvent(node, () -> getInnerText(node, client));
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public byte[] screenshot() {
        return Base64.getDecoder().decode(screenshot(node, client));
    }


//        @Override
//        public Optional<RuntimeGetProperties.InternalPropertyDescriptor> getProperty(String key) {
//            return getProperties(node, client)
//                    .stream()
//                    .filter(internalPropertyDescriptor -> key.equals(internalPropertyDescriptor.name()))
//                    .findFirst();
//        }
}