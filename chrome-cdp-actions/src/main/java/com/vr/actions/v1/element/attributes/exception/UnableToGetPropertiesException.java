package com.vr.actions.v1.element.attributes.exception;

public class UnableToGetPropertiesException extends RuntimeException {
    public UnableToGetPropertiesException(String message) {
        super(message);
    }

    public UnableToGetPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
