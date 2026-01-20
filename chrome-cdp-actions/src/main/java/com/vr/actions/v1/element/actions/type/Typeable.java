package com.vr.actions.v1.element.actions.type;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.actions.focus.Focusable;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.protocol.command.input.InputDispatchKeyEvent;
import com.vr.cdp.protocol.command.input.InputInsertText;

public interface Typeable extends Focusable {
    default void type(Element.Node node, CDPClient client, CharSequence charSequence) {
        try {
            focus(node, client);
            client.send(new InputInsertText(charSequence));
        } catch (Exception e) {
            throw new UnableToTypeException("Unable to type : ", e);
        }
    }

    default void typeIndividualChar(Element.Node node, CDPClient client, CharSequence charSequence) {
        typeIndividualCharWithTimeout(node, client, charSequence, 300);
    }

    default void typeIndividualChar(Element.Node node, CDPClient client, CharSequence charSequence, long millis) {
        typeIndividualCharWithTimeout(node, client, charSequence, millis);
    }

    private void typeIndividualCharWithTimeout(Element.Node node, CDPClient client, CharSequence charSequence, long millis) {
        try {
            focus(node, client);
            charSequence.chars().forEach(c -> {
                try {
                    client.sendAndWait(new InputDispatchKeyEvent("char", String.valueOf((char) c)));
                    Thread.sleep(millis);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new UnableToTypeException("Unable to type : ", e);
        }
    }
}
