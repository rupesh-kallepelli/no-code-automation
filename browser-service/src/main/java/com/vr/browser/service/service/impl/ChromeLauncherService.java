package com.vr.browser.service.service.impl;

import com.vr.actions.chrome.v1.ChromeLauncher;
import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.request.BrowserType;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.service.BrowserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
                .headless(false)
//                .remoteDebuggingPort(atomicInteger.getAndIncrement())
                .build();

        ChromeLauncher.ChromeDetails chromeDetails = chromeLauncher.launch();
        BrowserService.addNewBrowserProcess(chromeDetails.getId(), chromeDetails);

        return new BrowserSessionResponse(
                BrowserType.CHROME,
                chromeDetails.getWsUrl(),
                chromeDetails.getId()
        );
    }

}
