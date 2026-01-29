package com.vr.actions.v1.page.chromium.exception;

public class ScreenshotException extends RuntimeException {
    public ScreenshotException(String message) {
        super(message);
    }

    public ScreenshotException(String message, Throwable cause) {
        super(message, cause);
    }
}
