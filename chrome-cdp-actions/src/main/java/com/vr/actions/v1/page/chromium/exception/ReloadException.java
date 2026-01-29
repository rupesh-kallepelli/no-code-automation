package com.vr.actions.v1.page.chromium.exception;

public class ReloadException extends RuntimeException {
    public ReloadException(String message) {
        super(message);
    }

    public ReloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
