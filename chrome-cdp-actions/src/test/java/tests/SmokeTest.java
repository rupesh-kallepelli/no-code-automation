package tests;

import com.vr.actions.v1.chrome.ChromeLauncher;
import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.selector.Selector;
import com.vr.actions.v1.page.Page;
import com.vr.actions.v1.page.chromium.ChromePage;

import java.io.FileOutputStream;
import java.util.UUID;

public class SmokeTest {
    public static void main(String[] args) throws Exception {
        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .headless(false)
                .userDataDir("tmp/profile/"+ UUID.randomUUID())
                .binaryPath("C:\\Users\\kalle\\Softwares\\chrome-v143.0.7499.192\\chrome-win64\\chrome.exe")
                .remoteDebuggingPort(1000)
                .build();

        ChromeLauncher.ChromeDetails chromeDetails = chromeLauncher.launch();
        System.out.println(chromeDetails.getWsUrl());
        Page page = new ChromePage("12345", chromeDetails.getWsUrl());
        page.enable();
//        page.cast(
//                "jpeg",
//                90,
//                1920,
//                1080
//        );
        page.navigate("https://opensource-demo.orangehrmlive.com/");
//        Thread.sleep(3000);
        Element username = page.findElement(Selector.selectByXPath("//input[@name='username']"));
        username.typeIndividualCharacter("Admin");
        System.out.println(username.getAttributes());
        System.out.println(username.getProperties());
//        Thread.sleep(300);
        screenshot(page);
        Element password = page.findElement(Selector.selectByXPath("//input[@name='password']"));
        password.type("admin123");
//        Thread.sleep(300);
        screenshot(page);
//        page.findElement(Selector.selectByClass("orangehrm-login-forgot-header"))
//                .rightClick();
//        page.findElement(Selector.selectByTag("button")).rightClick();
        page.findElement(Selector.selectByXPath("//button[@type='submit']")).click();
        screenshot(page);
//        Thread.sleep(5000);

        Element element = page.findElement(Selector.selectByXPath("//span[text()='Admin']"));
        element.click();
//        Thread.sleep(3000);
        username.highlight();
        page.close();
        chromeDetails.getProcess().destroyForcibly();
    }

    private static void screenshot(Page page) throws Exception {
        byte[] bytes = page.screenshotPng();
        try (FileOutputStream fos = new FileOutputStream("screenshots/" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }
}
