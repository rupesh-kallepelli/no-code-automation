package com.vr.ai.automation.session;

public interface BrowserSession {

    void navigate(String url) throws Exception;

    void type(String selector, String text) throws Exception;

    void click(String selector) throws Exception;

    void waitForVisible(String selector, int timeoutMs) throws Exception;

    void close();
}
