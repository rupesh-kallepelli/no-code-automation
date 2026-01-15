package com.vr.actions.wait;

import com.vr.cdp.client.CDPClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PageLoadWaiter {

    private final CountDownLatch latch = new CountDownLatch(1);

    public PageLoadWaiter(CDPClient client) {
        client.onEvent("Page.loadEventFired",
                msg -> latch.countDown());
    }

    public void await(long timeoutMs) throws Exception {
        if (!latch.await(timeoutMs, TimeUnit.MILLISECONDS)) {
            throw new RuntimeException("Page load timeout");
        }
    }
}
