package com.vr.browser.service.service.impl;

import com.vr.actions.v1.chrome.ChromeLauncher;
import com.vr.browser.service.registry.BrowserRegistry;
import com.vr.browser.service.request.BrowserRequest;
import com.vr.browser.service.request.BrowserType;
import com.vr.browser.service.response.BrowserSessionResponse;
import com.vr.browser.service.service.BrowserService;
import com.vr.launcher.v1.BrowserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ServerSocket;
import java.util.UUID;

@Service
@Slf4j
public class ChromeLauncherService extends BrowserService {

    public static final String LOCAL_HOST = "127.0.0.1";
    private final String userDir;
    private final String ipAddress;
    private final String serverPort;
    private final boolean headless;
    private final BrowserRegistry browserRegistry;

    public ChromeLauncherService(
            @Value("${user.dir}") String userDir,
            @Value("${ip.address}") String ipAddress,
            @Value("${server.port}") String serverPort,
            @Value("${browser.mode}") boolean headless,
            BrowserRegistry browserRegistry
    ) {
        this.userDir = userDir;
        this.ipAddress = ipAddress;
        this.serverPort = serverPort;
        this.headless = headless;
        this.browserRegistry = browserRegistry;
    }

    @Override
    public BrowserSessionResponse launchBrowser(BrowserRequest browserRequest) throws Exception {

        ServerSocket serverSocket = new ServerSocket(0);
        String port = String.valueOf(serverSocket.getLocalPort());
        serverSocket.close();
        log.debug("Trying to create session with port : {}", port);

        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .userDataDir(userDir + "/chrome-profiles/" + UUID.randomUUID())
                .headless(headless)
                .remoteDebuggingAddress(LOCAL_HOST)
                .remoteDebuggingPort(port)
                .build();

        BrowserDetails chromeDetails = chromeLauncher.launch();
        browserRegistry.addNewBrowserProcess(chromeDetails.getId(), chromeDetails);

        String reWrittenUrl = super.replaceHostAndPort(
                chromeDetails.getWsUrl(),
                this.ipAddress,
                this.serverPort,
                chromeDetails.getId()
        );
        return new BrowserSessionResponse(
                chromeDetails.getId(),
                BrowserType.CHROME,
                reWrittenUrl,
                ipAddress,
                serverPort
        );
    }

}
