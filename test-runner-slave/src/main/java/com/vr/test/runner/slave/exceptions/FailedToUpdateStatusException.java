package com.vr.test.runner.slave.exceptions;

public class FailedToUpdateStatusException extends RuntimeException {
    public FailedToUpdateStatusException(String message) {
        super(message);
    }

    public FailedToUpdateStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
