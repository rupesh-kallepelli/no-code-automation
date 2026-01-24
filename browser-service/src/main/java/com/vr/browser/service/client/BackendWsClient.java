package com.vr.browser.service.client;

import java.nio.ByteBuffer;

public interface BackendWsClient {
    void sendText(String message);
    void sendBinary(ByteBuffer buffer);
    void close();
}
