package com.vr.launcher.v1;

import java.util.ArrayList;
import java.util.List;

public interface BrowserLauncher {
    List<String> usedWslUrl = new ArrayList<>();

    static void cleanWSUrl(String wsUrl) {
        usedWslUrl.remove(wsUrl);
    }

    BrowserDetails launch() throws Exception;
}
