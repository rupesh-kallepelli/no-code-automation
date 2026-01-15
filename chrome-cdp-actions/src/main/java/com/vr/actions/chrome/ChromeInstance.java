package com.vr.actions.chrome;

import com.vr.launcher.BrowserInstance;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChromeInstance implements BrowserInstance {

    private final Process process;
    private final String browserWsUrl;
    private final String pageWsUrl;
    private final File userDir;
    private volatile boolean closed = false;

    public ChromeInstance(
            Process process,
            String browserWsUrl,
            String pageWsUrl,
            File userDir
    ) {
        this.process = process;
        this.browserWsUrl = browserWsUrl;
        this.pageWsUrl = pageWsUrl;
        this.userDir = userDir;

        Runtime.getRuntime().addShutdownHook(
                new Thread(this::close)
        );
    }

    public String browserWebSocketUrl() {
        return browserWsUrl;
    }

    public String pageWebSocketUrl() {
        return pageWsUrl;
    }

    @Override
    public synchronized void close() {
        if (closed) return;
        closed = true;

        process.destroy();
        try {
            if (!process.waitFor(3, TimeUnit.SECONDS)) {
                process.destroyForcibly();
            }
        } catch (InterruptedException e) {
            process.destroyForcibly();
        }
        if (Objects.nonNull(userDir)) {
            userDir.delete();
        }
    }
}
