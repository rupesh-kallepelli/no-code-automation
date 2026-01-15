package com.vr.actions.chrome;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.actions.page.Page;
import com.vr.actions.page.chromium.ChromiumPage;
import com.vr.cdp.client.broadcast.BroadCaster;
import com.vr.launcher.BrowserLauncher;
import com.vr.actions.chrome.exception.BroadCasterCannotBeNull;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChromeLauncher implements BrowserLauncher {

    private final String chromeBinary;
    private final boolean headless;
    private final int remoteDebuggingPort;
    private final File userDataDir;
    private final List<String> extraArgs;
    private boolean enableScreenCasting = false;
    private final BroadCaster broadCaster;
    
    private ChromeLauncher(Builder b) {
        this.chromeBinary = resolveChromeBinary(b.binaryPath);
        this.headless = b.headless;
        this.remoteDebuggingPort = b.remoteDebuggingPort;
        this.userDataDir = b.userDataDir;
        this.extraArgs = b.extraArgs;
        this.enableScreenCasting = b.enableScreenCasting;
        if (enableScreenCasting && Objects.isNull(b.broadCaster))
            throw new BroadCasterCannotBeNull("You are trying to enable the broadcaster but didn't provide one," +
                    " please provide using broadcast() or disable enableScreenCasting(false)");
        this.broadCaster = b.broadCaster;
    }

    public Page launch() throws Exception {

        List<String> cmd = new ArrayList<>();
        cmd.add(chromeBinary);
        cmd.add("--remote-debugging-port=" + remoteDebuggingPort);
        cmd.add("--no-first-run");
        cmd.add("--no-default-browser-check");
        cmd.add("--disable-popup-blocking");
        cmd.add("--disable-background-networking");
        cmd.add("--disable-sync");
        cmd.add("--remote-debugging-address=127.0.0.1");
        cmd.add("--window-size=1920,1080");
        if (headless) {
            cmd.add("--headless=new");
        } else {
            cmd.add("--start-maximized");
        }

        if (userDataDir != null) {
            userDataDir.mkdirs();
            cmd.add("--user-data-dir=" + userDataDir.getAbsolutePath());
        }

        cmd.addAll(extraArgs);

        Process process = new ProcessBuilder(cmd)
                .redirectErrorStream(true)
                .start();

        String browserWs = waitForBrowserWs();
        String pageWs = waitForFirstPageWs();

        return new ChromiumPage(process, browserWs, pageWs, userDataDir, enableScreenCasting, broadCaster);
    }

    /* ------------------ CDP Discovery ------------------ */

    private String waitForBrowserWs() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < 50; i++) {
            try {
                HttpRequest req = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + remoteDebuggingPort + "/json/version")
                ).GET().build();

                HttpResponse<String> resp =
                        client.send(req, HttpResponse.BodyHandlers.ofString());

                JsonNode json = mapper.readTree(resp.body());
                return json.get("webSocketDebuggerUrl").asText();
            } catch (Exception e) {
                Thread.sleep(100);
            }
        }
        throw new RuntimeException("Chrome did not expose /json/version");
    }

    private String waitForFirstPageWs() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < 50; i++) {
            HttpRequest req = HttpRequest.newBuilder(
                    URI.create("http://localhost:" + remoteDebuggingPort + "/json")
            ).GET().build();

            HttpResponse<String> resp =
                    client.send(req, HttpResponse.BodyHandlers.ofString());

            JsonNode arr = mapper.readTree(resp.body());
            for (JsonNode node : arr) {
                if ("page".equals(node.get("type").asText())) {
                    return node.get("webSocketDebuggerUrl").asText();
                }
            }
            Thread.sleep(100);
        }
        throw new RuntimeException("No page target found");
    }

    /* ------------------ Binary Resolution ------------------ */

    private static String resolveChromeBinary(String provided) {
        if (provided != null) {
            File f = new File(provided);
            if (!f.exists()) {
                throw new IllegalArgumentException(
                        "Chrome binary not found: " + provided
                );
            }
            return f.getAbsolutePath();
        }

        String os = System.getProperty("os.name").toLowerCase();
        List<String> candidates = new ArrayList<>();

        if (os.contains("win")) {
            candidates.add("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
            candidates.add("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        } else if (os.contains("mac")) {
            candidates.add("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        } else {
            candidates.add("/usr/bin/google-chrome");
            candidates.add("/usr/bin/chromium");
            candidates.add("/usr/bin/chromium-browser");
        }

        for (String path : candidates) {
            if (new File(path).exists()) {
                return path;
            }
        }

        throw new RuntimeException("Chrome binary not found on system");
    }

    /* ------------------ Builder ------------------ */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String binaryPath;
        private boolean headless = false;
        private int remoteDebuggingPort = 9222;
        private File userDataDir;
        private final List<String> extraArgs = new ArrayList<>();
        private BroadCaster broadCaster;
        private boolean enableScreenCasting = false;

        public Builder enableScreenCasting(boolean enableScreenCasting) {
            this.enableScreenCasting = enableScreenCasting;
            return this;
        }

        public Builder broadcast(BroadCaster broadCaster) {
            this.broadCaster = broadCaster;
            return this;
        }

        public Builder binaryPath(String binaryPath) {
            this.binaryPath = binaryPath;
            return this;
        }

        public Builder headless(boolean headless) {
            this.headless = headless;
            return this;
        }

        public Builder remoteDebuggingPort(int port) {
            this.remoteDebuggingPort = port;
            return this;
        }

        public Builder userDataDir(String path) {
            this.userDataDir = new File(path);
            return this;
        }

        public Builder addArg(String arg) {
            this.extraArgs.add(arg);
            return this;
        }

        public ChromeLauncher build() {
            return new ChromeLauncher(this);
        }
    }
}
