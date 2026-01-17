package com.vr.actions.chrome.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.launcher.v1.BrowserDetails;
import com.vr.launcher.v1.BrowserLauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChromeLauncher implements BrowserLauncher {

    private final String chromeBinary;
    private final boolean headless;
    private final int remoteDebuggingPort;
    private final File userDataDir;
    private final List<String> extraArgs;

    private ChromeLauncher(Builder b) {
        this.chromeBinary = resolveChromeBinary(b.binaryPath);
        this.headless = b.headless;
        this.remoteDebuggingPort = b.remoteDebuggingPort;
        this.userDataDir = b.userDataDir;
        this.extraArgs = b.extraArgs;
    }


    public ChromeDetails launch() throws Exception {

        List<String> cmd = new ArrayList<>();
        cmd.add(chromeBinary);

        cmd.add("--remote-debugging-port=" + remoteDebuggingPort);
        cmd.add("--disable-dev-shm-usage");
        cmd.add("--no-sandbox");
        cmd.add("--no-first-run");
        cmd.add("--no-default-browser-check");
        cmd.add("--disable-popup-blocking");
        cmd.add("--disable-background-networking");
        cmd.add("--disable-extensions");
        cmd.add("--disable-default-apps");
        cmd.add("--disable-sync");
        cmd.add("--disable-component-update");
        cmd.add("--disable-gpu");
        cmd.add("--disable-features=Vulkan");
        cmd.add("--use-gl=swiftshader");
        cmd.add("--log-level=3");
        cmd.add("--enable-logging=stderr");
        cmd.add("--v=1");
        cmd.add("--remote-debugging-address=127.0.0.1");

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

        new Thread(() -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();

        return new ChromeDetails(waitForFirstPageWs(), Instant.now().toEpochMilli(), process, userDataDir);
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
            try {
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

                JsonNode arr = mapper.readTree(resp.body());
                for (JsonNode node : arr) {
                    String webSocketDebuggerUrl = node.get("webSocketDebuggerUrl").asText();
                    if ("page".equals(node.get("type").asText())
                            && !usedWslUrl.contains(webSocketDebuggerUrl)
                    ) {
                        usedWslUrl.add(webSocketDebuggerUrl);
                        return webSocketDebuggerUrl;
                    }
                }
            } catch (ConnectException ignored) {
                Thread.sleep(100);
            }
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
            candidates.add("/opt/chrome/chrome-linux64/chrome");
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


    public static class ChromeDetails implements BrowserDetails {
        private String wsUrl;
        private final Long id;
        private final Process process;
        private final File usrDir;

        public ChromeDetails(String wsUrl, long epochMilli, Process process, File usrDir) {
            this.wsUrl = wsUrl;
            this.id = epochMilli;
            this.process = process;
            this.usrDir = usrDir;
        }

        public String getWsUrl() {
            return wsUrl;
        }


        public Long getId() {
            return id;
        }

        public Process getProcess() {
            return process;
        }

        public File getUsrDir() {
            return usrDir;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ChromeDetails that = (ChromeDetails) o;
            return Objects.equals(wsUrl, that.wsUrl) && Objects.equals(id, that.id) && Objects.equals(process, that.process);
        }

        @Override
        public int hashCode() {
            return Objects.hash(wsUrl, id, process);
        }
    }

}
