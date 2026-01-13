package com.vr.cdp.client;

public class CDPResponse<R> {
    public int id;
    public R result;
    public Error error;

    public static class Error {
        public int code;
        public String message;
    }
}
