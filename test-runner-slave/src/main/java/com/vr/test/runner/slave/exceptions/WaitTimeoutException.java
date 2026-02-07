package com.vr.test.runner.slave.exceptions;

public class WaitTimeoutException extends RuntimeException {
    public WaitTimeoutException(String message) {
        super(message);
    }

    public WaitTimeoutException(Throwable cause) {
        super(cause);
    }

    public WaitTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
