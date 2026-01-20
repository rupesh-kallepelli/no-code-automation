package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RemoteObject(
        String type,
        String subtype,
        String className,
        Map<String, Object> value,
        String description,
        String objectId
) {}
