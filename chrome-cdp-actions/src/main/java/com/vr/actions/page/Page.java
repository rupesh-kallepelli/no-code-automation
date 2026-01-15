package com.vr.actions.page;

import com.vr.launcher.BrowserInstance;

public interface Page extends BrowserInstance {
    void enable() throws Exception;

    void disable() throws Exception;

    String navigate(String url) throws Exception;

    void reload() throws Exception;

    byte[] screenshotPng() throws Exception;

    void screenshot(String file) throws Exception;

    void click(String selector) throws Exception;

    void type(String selector, String text) throws Exception;

    void cast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    ) throws Exception;
}
