package com.vr.actions.v1.element;

import com.vr.actions.v1.element.actions.click.Clickable;
import com.vr.actions.v1.element.actions.highlight.Highlightable;
import com.vr.actions.v1.element.actions.move.Moveable;
import com.vr.actions.v1.element.actions.screenshot.Screenshot;
import com.vr.actions.v1.element.actions.type.Typeable;
import com.vr.actions.v1.element.attributes.ElementAttributes;
import com.vr.actions.v1.element.exception.StaleElementException;
import com.vr.actions.v1.element.selector.Selector;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.runtime.RuntimeGetProperties;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;

public interface Element extends Clickable,
        Typeable, Highlightable,
        ElementAttributes, Screenshot, Moveable {

    byte[] screenshot();

    enum State {
        STALE, ACTIVE
    }

    void click();

    void rightClick();

    void type(CharSequence charSequence);

    void typeIndividualCharacter(CharSequence sequence);

    void typeIndividualCharacter(CharSequence sequence, long millis);

    void scrollIntoView();

    void highlight();

    void hideHighlight();

    List<String> getAttributes() throws Exception;

    RuntimeGetProperties.Result getProperties() throws Exception;

    void invalidate();

    void moveToElement();

    void dragToElement(Element target);

    Node getNode();
//    Optional<RuntimeGetProperties.InternalPropertyDescriptor> getProperty(String key);

    class ElementImpl implements Element {
        private final Node node;
        private final CDPClient client;

        public ElementImpl(Node node, CDPClient client) {
            this.node = node;
            this.client = client;
        }

        private <T> T dispatchEvent(Node node, Callable<T> callable) throws Exception {
            if (node.state == State.STALE)
                throw new StaleElementException("Element you are trying to access is now stale, try create a one");
            return callable.call();
        }

        private void dispatchEvent(Node node, Runnable runnable) {
            if (node.state == State.STALE)
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
        public List<String> getAttributes() throws Exception {
            return dispatchEvent(node, () -> getAttributes(node, client));
        }

        @Override
        public RuntimeGetProperties.Result getProperties() throws Exception {
            return dispatchEvent(node, () -> getProperties(node, client));
        }

        @Override
        public void invalidate() {
            node.state = State.STALE;
        }

        @Override
        public void moveToElement() {
            dispatchEvent(node, () -> moveToElement(node, client));
        }

        @Override
        public void dragToElement(Element target) {
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

    enum IdentifiedBy {
        DOM,
        RUNTIME,
        BOTH
    }

    class Node {
        private int nodeId;
        private String objectId;
        private final Selector selector;
        private final IdentifiedBy identifiedBy;
        private State state;

        public Node(int nodeId, Selector selector, IdentifiedBy identifiedBy) {
            this.nodeId = nodeId;
            this.selector = selector;
            this.identifiedBy = identifiedBy;
            this.state = State.ACTIVE;
        }

        public Node(String objectId, Selector selector, IdentifiedBy identifiedBy) {
            this.objectId = objectId;
            this.selector = selector;
            this.identifiedBy = identifiedBy;
            this.state = State.ACTIVE;
        }

        public int getNodeId() {
            return nodeId;
        }

        public Selector getSelector() {
            return selector;
        }

        public String getObjectId() {
            return objectId;
        }

        public IdentifiedBy getIdentifiedBy() {
            return identifiedBy;
        }
    }
}
