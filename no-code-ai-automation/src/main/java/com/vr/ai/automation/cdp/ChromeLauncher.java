package com.vr.ai.automation.cdp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChromeLauncher {

    public static Process launch(
            String chromeBinary,
            boolean headless,
            int debuggingPort
    ) throws IOException {

        List<String> command = new ArrayList<>();
        command.add(chromeBinary);
        command.add("--remote-debugging-port=" + debuggingPort);
        command.add("--disable-gpu");
        command.add("--no-first-run");
        command.add("--no-default-browser-check");
        command.add("--disable-dev-shm-usage");
        command.add("--disable-background-networking");

        if (headless) {
            command.add("--headless=new");
        }

        command.add("about:blank");

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        return builder.start();
    }
}
