package com.vr.test.runner.slave.exceptions;

public class NoSuchTestCaseException extends RuntimeException {
    public NoSuchTestCaseException(String message) {
        super(message);
    }
}
