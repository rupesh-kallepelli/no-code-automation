package com.vr.actions.v1.element.actions.focus.exception;

public class UnableToFocusException extends RuntimeException {
    public UnableToFocusException(String message) {
        super(message);
    }

    public UnableToFocusException(String message, Throwable cause) {
        super(message, cause);
    }
}
