package com.vr.browser.service.service.impl;

import com.vr.actions.chrome.v1.ChromeLauncher;
import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.request.BrowserType;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.service.BrowserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChromeLauncherService implements BrowserService {

    private final AtomicInteger atomicInteger = new AtomicInteger(1000);
    @Value("${user.dir}")
    private String userDir;

    @Override
    public BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception {

        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .userDataDir(userDir + "/chrome-profiles/" + UUID.randomUUID())
                .headless(true)
//                .remoteDebuggingPort(atomicInteger.getAndIncrement())
                .build();

        ChromeLauncher.ChromeDetails chromeDetails = chromeLauncher.launch();
        BrowserService.addNewBrowserProcess(chromeDetails.getId(), chromeDetails);
        Thread loggerThread = getThread(chromeDetails);
        loggerThread.setDaemon(true);

        return new BrowserSessionResponse(
                BrowserType.CHROME,
                chromeDetails.getWsUrl(),
                chromeDetails.getId()
        );
    }

    private static Thread getThread(ChromeLauncher.ChromeDetails chromeDetails) {
        Thread loggerThread = new Thread(() -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(chromeDetails.getProcess().getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        loggerThread.start();
        return loggerThread;
    }

}
