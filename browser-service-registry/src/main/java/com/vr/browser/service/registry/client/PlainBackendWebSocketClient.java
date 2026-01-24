package com.vr.browser.service.registry.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class PlainBackendWebSocketClient extends WebSocketClient
        implements BackendWsClient {

    private Consumer<String> onText;
    private Consumer<ByteBuffer> onBinary;
    private Runnable onClose;

    public PlainBackendWebSocketClient(
            URI uri,
            Consumer<String> onText,
            Consumer<ByteBuffer> onBinary,
            Runnable onClose
    ) {
        super(uri);
        this.onText = onText;
        this.onBinary = onBinary;
        this.onClose = onClose;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("✅ Backend WS connected");
    }

    @Override
    public void onMessage(String message) {
        onText.accept(message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        onBinary.accept(bytes);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        onClose.run();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void sendText(String message) {
        send(message);
    }

    @Override
    public void sendBinary(ByteBuffer buffer) {
        send(buffer);
    }
}
