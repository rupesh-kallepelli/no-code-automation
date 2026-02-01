package com.vr.cdp.actions.v1.page;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.actions.v1.element.selector.Selector;

public interface Page extends AutoCloseable {
    void onEvent(String message);

    //  Wait for the current navigation to complete (if any).
    void waitForNavigation();

    String getSessionId();

    String navigate(String url);

    void reload() throws Exception;

    byte[] screenshot() throws Exception;

    byte[] screenshotFullPage() throws Exception;

    void cast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    );

    String getPageSource();

    Element findElement(Selector selector);

    Element findElement(Selector selector, long millis);
}