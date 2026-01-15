package tests;

import com.vr.actions.chrome.ChromeLauncher;
import com.vr.actions.page.Page;

import java.io.FileOutputStream;

public class SmokeTest {
    public static void main(String[] args) throws Exception {
        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
                .headless(false)
                .userDataDir("tmp/profile")
                .binaryPath("C:\\Users\\kalle\\Softwares\\chrome-v143.0.7499.192\\chrome-win64\\chrome.exe")
                .remoteDebuggingPort(1000)
                .enableScreenCasting(true)
                .broadcast(System.out::println)
                .build();

        Page page = chromeLauncher.launch();

        page.enable();
        page.cast(
                "jpeg",
                90,
                1920,
                1080
        );
        page.navigate("https://opensource-demo.orangehrmlive.com/");

        page.type("input[name='username']", "Admin");
        screenshot(page);
        Thread.sleep(5000);
        page.type("input[name='password']", "admin123");
        screenshot(page);
        Thread.sleep(5000);
        page.click("button[type='submit']");
        screenshot(page);
        Thread.sleep(5000);
        page.close();
    }

    private static void screenshot(Page page) throws Exception {
        byte[] bytes = page.screenshotPng();
        try (FileOutputStream fos = new FileOutputStream("screenshot" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }
}
