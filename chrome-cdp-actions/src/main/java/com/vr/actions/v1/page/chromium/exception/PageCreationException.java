package com.vr.actions.v1.page.chromium.exception;

public class PageCreationException extends RuntimeException {
    public PageCreationException(String message) {
        super(message);
    }

    public PageCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
