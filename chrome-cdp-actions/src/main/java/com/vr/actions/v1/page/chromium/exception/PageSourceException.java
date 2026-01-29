package com.vr.actions.v1.page.chromium.exception;

public class PageSourceException extends RuntimeException {
    public PageSourceException(String message) {
        super(message);
    }

  public PageSourceException(String message, Throwable cause) {
    super(message, cause);
  }
}
