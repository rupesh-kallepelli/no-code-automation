package com.vr.test.runner.slave.exceptions;

public class FailedToRegisterInCacheException extends RuntimeException {
    public FailedToRegisterInCacheException(String message) {
        super(message);
    }

    public FailedToRegisterInCacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
