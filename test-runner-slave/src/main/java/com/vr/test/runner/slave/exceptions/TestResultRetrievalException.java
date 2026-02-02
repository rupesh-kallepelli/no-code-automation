package com.vr.test.runner.slave.exceptions;

public class TestResultRetrievalException extends RuntimeException {
    public TestResultRetrievalException(String message) {
        super(message);
    }

    public TestResultRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
