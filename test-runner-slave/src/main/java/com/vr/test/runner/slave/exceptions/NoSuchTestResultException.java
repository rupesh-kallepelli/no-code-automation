package com.vr.test.runner.slave.exceptions;

public class NoSuchTestResultException extends RuntimeException {
    public NoSuchTestResultException(String message) {
        super(message);
    }

    public NoSuchTestResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
