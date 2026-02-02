package com.vr.browser.service.registry.exception;


public class BrowserSessionKillException extends RuntimeException {
    public BrowserSessionKillException(String message) {
        super(message);
    }

    public BrowserSessionKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
