package com.vr.test.runner.slave.exceptions;

public class BrowserConnectionException extends RuntimeException {
    public BrowserConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowserConnectionException(String message) {
        super(message);
    }
}
