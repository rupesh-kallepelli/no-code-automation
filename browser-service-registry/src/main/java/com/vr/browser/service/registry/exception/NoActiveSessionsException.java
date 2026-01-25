package com.vr.browser.service.registry.exception;

public class NoActiveSessionsException extends RuntimeException {
    public NoActiveSessionsException(String message) {
        super(message);
    }
}
