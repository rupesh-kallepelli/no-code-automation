package com.vr.test.runner.slave.util;

import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.test.runner.slave.model.WebSocketSessionDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScreencastBroadcaster implements BroadCaster {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSessionDetails>> sessions;


    public ScreencastBroadcaster() {
        this.sessions = new ConcurrentHashMap<>();
    }

    public void register(WebSocketSession session) {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

        String query = session.getHandshakeInfo().getUri().getQuery();
        String sessionId = query.substring(query.indexOf("sessionId=") + 10).split("&")[0];

        ConcurrentHashMap<String, WebSocketSessionDetails> viewers = sessions.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());

        viewers.put(session.getId(), // viewer identity
                new WebSocketSessionDetails(session.getId(), session, sink));

        // send() is called ONCE per connection
        session.send(sink.asFlux().map(session::textMessage)).subscribe();
    }

    public void unregister(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        String sessionId = query.substring(query.indexOf("sessionId=") + 10).split("&")[0];

        ConcurrentHashMap<String, WebSocketSessionDetails> viewers = sessions.get(sessionId);

        if (Objects.isNull(viewers)) return;

        WebSocketSessionDetails details = viewers.remove(session.getId());

        if (Objects.nonNull(details)) {
            Sinks.Many<String> sink = details.sinks();
            if (Objects.nonNull(sink)) {
                sink.tryEmitComplete();
            }
        }

        // remove cast only when last viewer disconnects
        if (viewers.isEmpty()) {
            sessions.remove(sessionId);
        }
    }

    public void broadcast(String sessionId, CharSequence sequence) {
        ConcurrentHashMap<String, WebSocketSessionDetails> viewers = sessions.get(sessionId);

        if (viewers == null) return;

        viewers.values().removeIf(details -> {
            Sinks.EmitResult result = details.sinks().tryEmitNext(sequence.toString());
            return result.isFailure();
        });
    }

    public void unregister(String sessionId) {

        ConcurrentHashMap<String, WebSocketSessionDetails> viewers = sessions.get(sessionId);

        if (Objects.isNull(viewers)) return;

        for (WebSocketSessionDetails webSocketSessionDetails : viewers.values()) {
            if (Objects.nonNull(webSocketSessionDetails)) {
                Sinks.Many<String> sink = webSocketSessionDetails.sinks();
                if (Objects.nonNull(sink)) {
                    sink.tryEmitComplete();
                }
                if (Objects.nonNull(webSocketSessionDetails.session()))
                    webSocketSessionDetails.session().close();
            }

        }
        viewers.clear();
    }
}
