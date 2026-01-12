package com.vr.ai.automation.cdp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChromiumLauncher {

    public static void launch(String chromeBinary, boolean headless, int port) throws Exception {

        Path profileDir = Files.createTempDirectory("cdp-profile");

        List<String> command = new ArrayList<>();

        // 🔥 USE REAL BINARY PATH
        command.add(chromeBinary);
        command.add("--inprivate");
        // 🔥 REQUIRED FOR EDGE CDP
        command.add("--user-data-dir=" + profileDir.toAbsolutePath());

        command.add("--remote-debugging-address=127.0.0.1");
        command.add("--remote-debugging-port=" + port);

        // ❌ REMOVE inprivate (it breaks CDP)
        // command.add("--inprivate");

        command.add("--no-first-run");
        command.add("--no-default-browser-check");
        command.add("--disable-extensions");
        command.add("--disable-background-networking");
        command.add("--disable-sync");

        if (headless) {
            command.add("--headless=new");
        }

        new ProcessBuilder(command).start();
    }
}
