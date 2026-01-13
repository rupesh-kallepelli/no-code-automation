package tests;

import com.vr.actions.ActionContext;
import com.vr.actions.WebActions;
import com.vr.actions.page.PageActions;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.ws.RawCDPClient;
import com.vr.launcher.chrome.ChromeInstance;
import com.vr.launcher.chrome.ChromeLauncher;

import java.io.FileOutputStream;

public class SmokeTest {
    public static void main(String[] args) throws Exception {
        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .headless(true)
                .userDataDir("tmp/profile")
                .binaryPath("C:\\Users\\kalle\\Softwares\\chrome-v143.0.7499.192\\chrome-win64\\chrome.exe")
                .remoteDebuggingPort(1000)
                .build();
        ChromeInstance instance = chromeLauncher.launch();
        CDPClient client = new RawCDPClient(instance.pageWebSocketUrl());
        System.out.println(instance.pageWebSocketUrl());
        ActionContext ctx = new ActionContext(client);

        PageActions page = new PageActions(ctx);
        WebActions web = new WebActions(ctx);

        page.enable();
        page.navigate("https://opensource-demo.orangehrmlive.com/");

        web.type("input[name='username']", "Admin");
        screenshot(page);
        Thread.sleep(5000);
        web.type("input[name='password']", "admin123");
        screenshot(page);
        Thread.sleep(5000);
        web.click("button[type='submit']");
        screenshot(page);
        Thread.sleep(5000);
        instance.close();
    }

    private static void screenshot(PageActions page) throws Exception {
        byte[] bytes = page.screenshotPng();
        try (FileOutputStream fos = new FileOutputStream("screenshot" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }
}
