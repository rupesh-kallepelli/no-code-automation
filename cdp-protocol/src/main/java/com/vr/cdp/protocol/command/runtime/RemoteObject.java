package com.vr.cdp.protocol.command.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RemoteObject(
        String type,
        String subtype,
        Object value,
        String description,
        String objectId
) {}
