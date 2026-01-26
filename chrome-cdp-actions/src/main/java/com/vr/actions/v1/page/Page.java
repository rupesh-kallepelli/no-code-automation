package com.vr.actions.v1.page;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.selector.Selector;

import java.util.Objects;
import java.util.function.Supplier;

public interface Page extends AutoCloseable {
    void onEvent(String message);

    enum PageState {
        BLOCKED,          // navigating / loading
        BROWSER_LOADED,   // frameStoppedLoading
        APP_LOADING,      // waiting for network / DOM
        READY,
        DETACHED
    }



    class PageWaiter {
        private final Object monitor = new Object();

        public <T> T waitUntilReadyAndThenExecute(Page page, Supplier<T> supplier) {
            synchronized (monitor) {

                while (!Objects.equals(page.getPageState(), PageState.READY)) {
//                    if (page.getPageState() == PageState.DETACHED) {
//                        throw new IllegalStateException("Page is detached");
//                    }
                    try {
                        monitor.wait();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted due to unknown error", e);
                    }
                }
            }
            return supplier.get();
        }

        public void release() {
            synchronized (monitor) {
                monitor.notify();
            }
        }
    }

    PageState getPageState();

    void setPageState(PageState pageState);
    String getId();

    void enable() throws Exception;

    void disable() throws Exception;

    String navigate(String url);

    void reload() throws Exception;

    byte[] screenshot() throws Exception;


    byte[] screenshotFullPage() throws Exception;

    void click(String selector) throws Exception;

    void type(String selector, String text) throws Exception;

    void cast(
            String format,
            Integer quality,
            Integer maxWidth,
            Integer maxHeight
    ) throws Exception;

    Element findElement(Selector selector);
}
