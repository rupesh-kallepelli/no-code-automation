package com.vr.actions.v1.element.actions.screenshot.exception;

public class FailedToTakeException extends RuntimeException {
    public FailedToTakeException(String message) {
        super(message);
    }

    public FailedToTakeException(String message, Exception e) {
        super(message, e);
    }
}
