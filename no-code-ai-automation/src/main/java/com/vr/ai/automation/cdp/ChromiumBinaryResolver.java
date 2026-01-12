package com.vr.ai.automation.cdp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChromiumBinaryResolver {

    public static String resolve(BrowserType browser, String overridePath) {

        if (overridePath != null && !overridePath.isBlank()) {
            if (!Files.exists(Path.of(overridePath))) {
                throw new RuntimeException("Browser binary not found at " + overridePath);
            }
            return overridePath;
        }

        String os = System.getProperty("os.name").toLowerCase();

        return switch (browser) {
            case EDGE -> resolveEdge(os);
            case CHROME -> resolveChrome(os);
        };
    }

    private static String resolveChrome(String os) {
        if (os.contains("win")) {
            return find(List.of(
                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
            ));
        }
        if (os.contains("mac")) {
            return find(List.of(
                "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
            ));
        }
        return find(List.of(
            "/usr/bin/google-chrome",
            "/usr/bin/google-chrome-stable"
        ));
    }

    private static String resolveEdge(String os) {
        if (os.contains("win")) {
            return find(List.of(
                "C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe",
                "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe"
            ));
        }
        if (os.contains("mac")) {
            return find(List.of(
                "/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge"
            ));
        }
        return find(List.of(
            "/usr/bin/microsoft-edge",
            "/usr/bin/microsoft-edge-stable"
        ));
    }

    private static String find(List<String> paths) {
        return paths.stream()
                .filter(p -> Files.exists(Path.of(p)))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Browser binary not found. Please configure override path")
                );
    }
}
