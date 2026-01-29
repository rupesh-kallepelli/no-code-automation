package com.vr.launcher.v1;

public interface BrowserInstance extends AutoCloseable {
    String browserWebSocketUrl();

    String pageWebSocketUrl();
}
