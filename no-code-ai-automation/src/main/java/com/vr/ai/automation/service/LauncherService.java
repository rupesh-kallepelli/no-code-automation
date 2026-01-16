package com.vr.ai.automation.service;

import com.vr.actions.chrome.ChromeLauncher;
import com.vr.actions.page.Page;
import com.vr.ai.automation.util.ScreencastBroadcaster;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class LauncherService {
    private Page instance;
    private final ScreencastBroadcaster screencastBroadcaster;

    public LauncherService(ScreencastBroadcaster screencastBroadcaster) {
        this.screencastBroadcaster = screencastBroadcaster;
    }

    public Page getChromeInstance(boolean broadCast) throws Exception {
        ChromeLauncher.Builder chromeLauncher = ChromeLauncher.builder()
                .headless(false)
                .userDataDir("tmp/profile")
//                .binaryPath("C:\\Users\\kalle\\Softwares\\chrome-v143.0.7499.192\\chrome-win64\\chrome.exe")
                .remoteDebuggingPort(9222);
        if (broadCast) chromeLauncher.enableScreenCasting(true).broadcast(screencastBroadcaster);
        this.instance = chromeLauncher.build().launch();
        return this.instance;
    }

    public void closeInstance() throws Exception {
        if (Objects.nonNull(this.instance))
            this.instance.close();
    }
}
