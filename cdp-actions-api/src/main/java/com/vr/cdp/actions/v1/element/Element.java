package com.vr.cdp.actions.v1.element;

import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.cdp.protocol.command.runtime.RuntimeGetProperties;

import java.util.List;

public interface Element {

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

    List<String> getAttributes();

    RuntimeGetProperties.Result getProperties();

    void invalidate();

    void moveToElement();

    void dragToElement(Element target);

    String getText();

    Node getNode();


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

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

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
