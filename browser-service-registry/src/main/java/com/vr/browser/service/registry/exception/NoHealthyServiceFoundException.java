package com.vr.browser.service.registry.exception;

public class NoHealthyServiceFoundException extends RuntimeException {
    public NoHealthyServiceFoundException(String message) {
        super(message);
    }
}
