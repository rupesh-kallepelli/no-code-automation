package com.vr.cdp.protocol.command.response;

public class CDPResponse<R> {
    public int id;
    public R result;
    public Error error;

    public static class Error {
        public int code;
        public String message;
    }
}
