package com.vr.test.runner.slave.util;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ScreencastSocketHandler implements WebSocketHandler {

    private final ScreencastBroadcaster broadcaster;

    public ScreencastSocketHandler(ScreencastBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @Override
    public @NonNull Mono<Void> handle(WebSocketSession session) {
        broadcaster.register(session);
        return session.receive()
                .doFinally(signal -> broadcaster.unregister(session))
                .then();
    }
}
