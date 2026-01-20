package com.vr.actions.v1.element.finder.dom.exception;

public class ElementNotFound extends RuntimeException {
    public ElementNotFound(String message) {
        super(message);
    }

    public ElementNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
