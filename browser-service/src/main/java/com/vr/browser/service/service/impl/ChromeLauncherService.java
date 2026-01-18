package com.vr.browser.service.service.impl;

import com.vr.actions.chrome.v1.ChromeLauncher;
import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.request.BrowserType;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.service.BrowserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ServerSocket;
import java.util.UUID;

@Service
@Slf4j
public class ChromeLauncherService implements BrowserService {

    private final String userDir;
    private final String cdpWSHost;

    public ChromeLauncherService(
            @Value("${user.dir}") String userDir,
            @Value("${cdp.ws.host}") String cdpWSHost
    ) {
        this.userDir = userDir;
        this.cdpWSHost = cdpWSHost;
    }

    @Override
    public BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception {

        ServerSocket serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();
        serverSocket.close();
        log.debug("Trying to create session with port : {}", port);

        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .userDataDir(userDir + "/chrome-profiles/" + UUID.randomUUID())
                .headless(false)
                .remoteDebuggingAddress("127.0.0.1")
                .remoteDebuggingPort(port)
                .build();

        ChromeLauncher.ChromeDetails chromeDetails = chromeLauncher.launch();
        BrowserService.addNewBrowserProcess(chromeDetails.getId(), chromeDetails);

        String reWrittenUrl = replaceHostAndPort(chromeDetails.getWsUrl(), cdpWSHost, port);
        return new BrowserSessionResponse(
                chromeDetails.getId(),
                BrowserType.CHROME,
                reWrittenUrl,
                cdpWSHost + "/proxy/" + port + "/",
                port
        );
    }

}
