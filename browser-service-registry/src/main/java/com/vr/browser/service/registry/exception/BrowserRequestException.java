package com.vr.browser.service.registry.exception;


public class BrowserRequestException extends RuntimeException {
    public BrowserRequestException(String message) {
        super(message);
    }

    public BrowserRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
