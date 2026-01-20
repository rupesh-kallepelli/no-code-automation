package com.vr.actions.v1.element.attributes.exception;

public class UnableToGetAttributesException extends RuntimeException {
    public UnableToGetAttributesException(String message) {
        super(message);
    }

    public UnableToGetAttributesException(String message, Throwable cause) {
        super(message, cause);
    }
}
