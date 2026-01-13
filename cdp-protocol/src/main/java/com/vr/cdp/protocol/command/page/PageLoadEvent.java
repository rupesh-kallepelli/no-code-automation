package com.vr.cdp.protocol.command.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PageLoadEvent(double timestamp) {}
