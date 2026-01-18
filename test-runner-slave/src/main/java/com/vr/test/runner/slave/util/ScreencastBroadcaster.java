package com.vr.test.runner.slave.util;

import com.vr.cdp.client.broadcast.BroadCaster;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ScreencastBroadcaster implements BroadCaster {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    public void register(WebSocketSession session) {
        sessions.add(session);
    }

    public void unregister(WebSocketSession session) {
        sessions.remove(session);
    }

    public void broadcast(CharSequence charSequence) {
        TextMessage message = new TextMessage(charSequence);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
