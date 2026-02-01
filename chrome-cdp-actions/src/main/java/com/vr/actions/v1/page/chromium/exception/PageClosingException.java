package com.vr.actions.v1.page.chromium.exception;

public class PageClosingException extends RuntimeException {
    public PageClosingException(String message) {
        super(message);
    }

    public PageClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
