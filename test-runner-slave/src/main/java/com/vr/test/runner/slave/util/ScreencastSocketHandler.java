package com.vr.test.runner.slave.util;

import com.vr.test.runner.slave.registry.TestCaseRegistry;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ScreencastSocketHandler implements WebSocketHandler {

    private final ScreencastBroadcaster broadcaster;
    private final TestCaseRegistry testCaseRegistry;

    public ScreencastSocketHandler(ScreencastBroadcaster broadcaster, TestCaseRegistry testCaseRegistry) {
        this.testCaseRegistry = testCaseRegistry;
        this.broadcaster = broadcaster;
    }

    @Override
    public @NonNull Mono<Void> handle(WebSocketSession session) {
        log.debug("Handling the websocket connect with id {}", session.getId());
        String query = session.getHandshakeInfo().getUri().getQuery();
        String sessionId = query.substring(query.indexOf("sessionId=") + 10).split("&")[0];
        if (!testCaseRegistry.getRunningTestCaseIds().contains(sessionId)) {
            log.debug("Test case Session {} not found disconnecting from the client with id {}", sessionId, session.getId());
            return Mono.empty();
        }
        broadcaster.register(session);
        log.debug("Registered websocket session with id {} and test case id {}", session.getId(), sessionId);
        return session.receive()
                .doFinally(signal -> broadcaster.unregister(session))
                .then();
    }
}
