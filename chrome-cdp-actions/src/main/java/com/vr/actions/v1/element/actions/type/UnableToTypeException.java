package com.vr.actions.v1.element.actions.type;

public class UnableToTypeException extends RuntimeException {
    public UnableToTypeException(String message) {
        super(message);
    }

    public UnableToTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
