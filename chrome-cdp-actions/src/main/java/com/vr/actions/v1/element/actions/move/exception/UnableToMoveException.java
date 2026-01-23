package com.vr.actions.v1.element.actions.move.exception;

public class UnableToMoveException extends RuntimeException {
    public UnableToMoveException(String message) {
        super(message);
    }

    public UnableToMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
