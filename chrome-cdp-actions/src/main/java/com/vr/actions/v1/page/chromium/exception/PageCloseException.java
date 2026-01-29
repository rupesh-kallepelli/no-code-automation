package com.vr.actions.v1.page.chromium.exception;

public class PageCloseException extends RuntimeException {
    public PageCloseException(String message) {
        super(message);
    }

    public PageCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
