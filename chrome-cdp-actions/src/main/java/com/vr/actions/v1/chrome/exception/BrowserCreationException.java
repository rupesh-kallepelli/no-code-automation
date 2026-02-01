package com.vr.actions.v1.chrome.exception;

public class BrowserCreationException extends RuntimeException {
    public BrowserCreationException(String message) {
        super(message);
    }

    public BrowserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
