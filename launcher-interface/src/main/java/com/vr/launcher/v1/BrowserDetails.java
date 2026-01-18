package com.vr.launcher.v1;

import java.io.File;

public interface BrowserDetails {
    String getWsUrl();

    String getId();

    Process getProcess();

    File getUsrDir();

    String getAddress();

    int getPort();
}
