package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RuntimeExceptionThrown(
        ExceptionDetails exceptionDetails
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExceptionDetails(
            String text,
            int lineNumber,
            int columnNumber
    ) {}
}
