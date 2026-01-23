package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RuntimeConsoleAPICalled(
        String type,
        List<RemoteObject> args
) {}
