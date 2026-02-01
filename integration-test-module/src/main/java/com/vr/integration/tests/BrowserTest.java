package com.vr.integration.tests;

import com.vr.actions.v1.chrome.ChromeBrowser;
import com.vr.cdp.actions.v1.browser.Browser;
import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.chrome.launcher.v1.chrome.ChromeLauncher;
import com.vr.launcher.v1.BrowserDetails;
import com.vr.launcher.v1.BrowserLauncher;

import java.util.List;
import java.util.UUID;

public class BrowserTest {
    public static void main(String[] args) throws Exception {
        BrowserLauncher launcher = ChromeLauncher.builder()
                .headless(false)
                .remoteDebuggingPort("1000")
                .userDataDir("tmp/profile/" + UUID.randomUUID())
                .build();

        BrowserDetails browserDetails = launcher.launch();
        Browser browser = new ChromeBrowser("123", browserDetails.getBrowserWsUrl());

        Page page = browser.getPage();

        page.navigate("https://demoqa.com/frames");
        page.reload();
        System.out.println(page.getPageSource());
        Element about = page.findElement(Selector.selectByXPath("//*[@id='sampleHeading']"));
        about.rightClick();
        System.out.println(about.getText());
        List<String> attributes = about.getAttributes();
        System.out.println(attributes);

        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        Element username = page.findElement(Selector.selectByCssSelector("input[name='username']"), 10000);
        username.type("Admin");

        Element password = page.findElement(Selector.selectByCssSelector("input[name='password']"));
        password.type("admin123");

        Element button = page.findElement(Selector.selectByCssSelector("button"));
        button.click();

        page.close();

        browserDetails.getProcess().destroyForcibly();

    }
}
