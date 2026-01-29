package com.vr.launcher.v1;

import java.io.File;

public interface BrowserDetails {
    String getBrowserWsUrl();

    String getPageWsUrl();

    String getId();

    Process getProcess();

    File getUsrDir();

    String getAddress();

    String getPort();
}
