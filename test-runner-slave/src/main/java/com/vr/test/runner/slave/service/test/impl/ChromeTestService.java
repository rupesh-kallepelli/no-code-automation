package com.vr.test.runner.slave.service.test.impl;

import com.vr.actions.chrome.ChromeLauncher;
import com.vr.actions.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequestScope
public class ChromeTestService extends ChromiumTestService {

    @Override
    public Page launch() {
        try {
            return getChromeLauncher().launch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ChromeLauncher getChromeLauncher() throws IOException {
        return ChromeLauncher
                .builder()
                .headless(false)
                .userDataDir(
                        Files.createTempDirectory(System.getProperty("user.dir")).toAbsolutePath().toString()
                )
                .remoteDebuggingPort(1000)
                .build();
    }

}
