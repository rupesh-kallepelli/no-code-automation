package com.vr.browser.service.exception;

public class NoSuchSession extends RuntimeException {
    public NoSuchSession(String message) {
        super(message);
    }
}
