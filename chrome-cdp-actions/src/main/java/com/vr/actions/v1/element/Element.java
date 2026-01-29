package com.vr.actions.v1.element;

import com.vr.actions.v1.element.actions.click.Clickable;
import com.vr.actions.v1.element.actions.highlight.Highlightable;
import com.vr.actions.v1.element.actions.move.Moveable;
import com.vr.actions.v1.element.actions.screenshot.Screenshot;
import com.vr.actions.v1.element.actions.type.Typeable;
import com.vr.actions.v1.element.attributes.ElementAttributes;
import com.vr.cdp.protocol.command.runtime.RuntimeGetProperties;

import java.util.List;

public interface Element extends com.vr.cdp.actions.v1.element.Element, Clickable,
        Typeable, Highlightable,
        ElementAttributes, Screenshot, Moveable {

    byte[] screenshot();


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

    void dragToElement(com.vr.cdp.actions.v1.element.Element target);

    String getText();

    Node getNode();

}
