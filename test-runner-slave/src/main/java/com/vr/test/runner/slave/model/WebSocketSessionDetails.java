package com.vr.test.runner.slave.model;

import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

public record WebSocketSessionDetails(
        String sessionId,
        WebSocketSession session,
        Sinks.Many<String> sinks
) {
}
