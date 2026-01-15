package com.vr.launcher;

public interface BrowserInstance extends AutoCloseable {
    String browserWebSocketUrl();

    String pageWebSocketUrl();
}
