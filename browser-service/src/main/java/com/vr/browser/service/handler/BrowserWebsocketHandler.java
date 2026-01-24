package com.vr.browser.service.handler;

import com.vr.browser.service.client.PlainBackendWebSocketClient;
import com.vr.browser.service.registry.BrowserRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.URI;

@Component
@Slf4j
public class BrowserWebsocketHandler implements WebSocketHandler {

    private final BrowserRegistry browserRegistry;

    public BrowserWebsocketHandler(BrowserRegistry browserRegistry) {
        this.browserRegistry = browserRegistry;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        URI requestUri = session.getHandshakeInfo().getUri();
        String query = requestUri.getQuery();

        URI browserSession =
                browserRegistry.getURIOf(query.substring(query.indexOf("=") + 1));

        Sinks.Many<WebSocketMessage> outboundSink =
                Sinks.many().unicast().onBackpressureBuffer();

        PlainBackendWebSocketClient backendClient =
                new PlainBackendWebSocketClient(
                        browserSession,
                        text -> outboundSink.tryEmitNext(session.textMessage(text)),
                        binary -> outboundSink.tryEmitNext(
                                session.binaryMessage(b -> b.wrap(binary))
                        ),
                        outboundSink::tryEmitComplete
                );

        Mono<Void> frontendToBackend =
                session.receive()
                        .doOnNext(msg -> {
                            if (msg.getType() == WebSocketMessage.Type.TEXT) {
                                backendClient.sendText(msg.getPayloadAsText());
                            } else if (msg.getType() == WebSocketMessage.Type.BINARY) {
                                backendClient.sendBinary(msg.getPayload().asByteBuffer());
                            }
                        })
                        .doFinally(sig -> backendClient.close())
                        .then();

        Mono<Void> backendToFrontend =
                session.send(outboundSink.asFlux());

        return Mono.when(frontendToBackend, backendToFrontend);
    }

}
