package com.vr.cdp.protocol.command.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NetworkLoadingFinished(String requestId) {}
