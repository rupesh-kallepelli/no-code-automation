package com.vr.browser.service.registry.handler;

import com.vr.browser.service.registry.client.PlainBackendWebSocketClient;
import com.vr.browser.service.registry.util.BackendServiceRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class ProxyWebSocketHandler implements WebSocketHandler {

    private final BackendServiceRegistry registry;

    public ProxyWebSocketHandler(BackendServiceRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        URI backendUri = URI.create(registry.getPodUrl("pod-1"));

        PlainBackendWebSocketClient backendClient =
                new PlainBackendWebSocketClient(
                        backendUri,
                        // backend → frontend
                        text -> session.send(
                                Mono.just(session.textMessage(text))
                        ).subscribe(),

                        binary -> session.send(
                                Mono.just(session.binaryMessage(b -> b.wrap(binary)))
                        ).subscribe(),

                        // backend closed
                        () -> session.close().subscribe()
                );

        // frontend → backend
        return session.receive()
                .doOnNext(msg -> {
                    if (msg.getType() == WebSocketMessage.Type.TEXT) {
                        backendClient.sendText(msg.getPayloadAsText());
                    } else if (msg.getType() == WebSocketMessage.Type.BINARY) {
                        backendClient.sendBinary(msg.getPayload().asByteBuffer());
                    }
                })
                .doFinally(sig -> backendClient.close())
                .then();
    }

}
