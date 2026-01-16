package com.vr.browser.service.exception;

public class NoBrowserSupportYetException extends RuntimeException {
    public NoBrowserSupportYetException(String message) {
        super(message);
    }
}
