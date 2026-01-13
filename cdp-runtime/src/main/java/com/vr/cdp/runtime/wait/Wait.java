package com.vr.cdp.runtime.wait;

import java.util.function.BooleanSupplier;

public class Wait {

    public static void until(BooleanSupplier condition,
                             long timeoutMs,
                             long pollMs) throws Exception {

        long end = System.currentTimeMillis() + timeoutMs;

        while (System.currentTimeMillis() < end) {
            if (condition.getAsBoolean()) return;
            Thread.sleep(pollMs);
        }
        throw new RuntimeException("Wait timeout");
    }
}
