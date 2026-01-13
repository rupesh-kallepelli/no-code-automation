package com.vr.ai.automation.service;

import com.vr.launcher.chrome.ChromeInstance;
import com.vr.launcher.chrome.ChromeLauncher;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class LauncherService {
    private ChromeInstance instance;

    public ChromeInstance getChromeInstance() throws Exception {
        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .headless(false)
                .userDataDir("tmp/profile")
                .binaryPath("C:\\Users\\kalle\\Softwares\\chrome-v143.0.7499.192\\chrome-win64\\chrome.exe")
                .remoteDebuggingPort(1000)
                .build();
        this.instance = chromeLauncher.launch();
        return this.instance;
    }

    public void closeInstance() {
        if (Objects.nonNull(this.instance))
            this.instance.close();
    }
}
