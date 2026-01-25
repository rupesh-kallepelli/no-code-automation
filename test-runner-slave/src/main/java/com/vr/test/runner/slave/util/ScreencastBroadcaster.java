package com.vr.test.runner.slave.util;

import com.vr.cdp.client.broadcast.BroadCaster;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScreencastBroadcaster implements BroadCaster {

    private final ConcurrentHashMap<WebSocketSession, Sinks.Many<String>> sessions =
            new ConcurrentHashMap<>();

    public void register(WebSocketSession session) {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

        sessions.put(session, sink);

        // send() is called ONCE here
        session.send(
                sink.asFlux().map(session::textMessage)
        ).subscribe();
    }

    public void unregister(WebSocketSession session) {
        Sinks.Many<String> sink = sessions.remove(session);
        if (sink != null) {
            sink.tryEmitComplete();
        }
    }

    public void broadcast(CharSequence sequence) {
        for (Sinks.Many<String> sink : sessions.values()) {
            sink.tryEmitNext(sequence.toString());
        }
    }
}
