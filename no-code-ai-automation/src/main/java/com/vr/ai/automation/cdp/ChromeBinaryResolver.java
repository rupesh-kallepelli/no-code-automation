package com.vr.ai.automation.cdp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChromeBinaryResolver {

    public static String resolve(String overridePath) {

        // 1️⃣ Explicit override always wins
        if (overridePath != null && !overridePath.isBlank()) {
            if (!Files.exists(Path.of(overridePath))) {
                throw new RuntimeException("Chrome binary not found at " + overridePath);
            }
            return overridePath;
        }

        String os = System.getProperty("os.name").toLowerCase();

        // 2️⃣ OS-specific defaults
        if (os.contains("win")) {
            return findFirstExisting(List.of(
                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
            ));
        }

        if (os.contains("mac")) {
            return findFirstExisting(List.of(
                "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
            ));
        }

        // Linux
        return findFirstExisting(List.of(
            "/usr/bin/google-chrome",
            "/usr/bin/google-chrome-stable",
            "/usr/bin/chromium",
            "/usr/bin/chromium-browser"
        ));
    }

    private static String findFirstExisting(List<String> paths) {
        return paths.stream()
                .filter(p -> Files.exists(Path.of(p)))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Chrome binary not found. Please configure browser.chrome.binary")
                );
    }
}
