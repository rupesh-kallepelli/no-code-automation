package com.vr.test.runner.slave.exceptions;

public class ClientSideException extends RuntimeException {
    public ClientSideException(String message) {
        super(message);
    }

    public ClientSideException(String message, Throwable cause) {
        super(message, cause);
    }
}
