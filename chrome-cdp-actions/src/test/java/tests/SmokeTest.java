//package tests;
//
//import com.vr.actions.v1.chrome.ChromeLauncher;
//import com.vr.actions.v1.page.chromium.ChromePage;
//import com.vr.cdp.actions.v1.element.Element;
//import com.vr.cdp.actions.v1.element.selector.Selector;
//import com.vr.cdp.actions.v1.page.Page;
//
//import java.io.FileOutputStream;
//import java.util.List;
//import java.util.UUID;
//
//public class SmokeTest {
//    public static void main(String[] args) throws Exception {
//        ChromeLauncher chromeLauncher = ChromeLauncher.builder()
//                .headless(false)
//                .userDataDir("tmp/profile/" + UUID.randomUUID())
//                .binaryPath("C:\\Users\\kalle\\Softwares\\chrome-v143.0.7499.192\\chrome-win64\\chrome.exe")
//                .remoteDebuggingPort("1000")
//                .build();
//
//        ChromeLauncher.ChromeDetails chromeDetails = chromeLauncher.launch();
//        System.out.println(chromeDetails.getPageWsUrl());
//
//        Page page = new ChromePage("12345", chromeDetails.getPageWsUrl());
//
////        page.cast(
////                "jpeg",
////                90,
////                1920,
////                1080
////        );
////
//        page.navigate("https://demoqa.com/frames");
//        page.reload();
//        System.out.println(page.getPageSource());
//        Element about = page.findElement(Selector.selectByXPath("//*[@id='sampleHeading']"));
//        about.rightClick();
//        System.out.println(about.getText());
//        List<String> attributes = about.getAttributes();
//        System.out.println(attributes);
////        Element imageToDrag = page.findElement(Selector.selectByCssSelector(".ui-draggable-handle"));
////        imageToDrag.rightClick();
////        Thread.sleep(5000);
////        Element trashDestination = page.findElement(Selector.selectByXPath("//div[@class='ui-widget-content ui-state-default ui-droppable']"));
////        imageToDrag.dragToElement(trashDestination);
//        page.navigate("https://opensource-demo.orangehrmlive.com/");
////        Thread.sleep(3000);
//        Element username = page.findElement(Selector.selectByXPath("//input[@name='username']"));
//        screenshot(username);
//        username.typeIndividualCharacter("Admin");
//        screenshot(username);
//        System.out.println(username.getAttributes());
//        System.out.println(username.getProperties());
////        Thread.sleep(300);
//        screenshot(page);
//        Element password = page.findElement(Selector.selectByXPath("//input[@name='password']"));
//        screenshot(password);
//        password.type("admin123");
//        screenshot(password);
////        Thread.sleep(300);
//        screenshot(page);
////        page.findElement(Selector.selectByClass("orangehrm-login-forgot-header"))
////                .rightClick();
////        page.findElement(Selector.selectByTag("button")).rightClick();
//        Element button = page.findElement(Selector.selectByXPath("//button[@type='submit']"));
//        screenshot(button);
//        System.out.println(button.getText());
//        button.click();
//        screenshot(page);
//        page.waitForNavigation();
////        Thread.sleep(5000);
//
//        Element element = page.findElement(Selector.selectByXPath("//span[text()='Admin']"));
//        screenshot(element);
//        element.click();
////        Thread.sleep(3000);
////        username.highlight();
//        page.close();
//        chromeDetails.getProcess().destroyForcibly();
//    }
//
//    private static void screenshot(Page page) throws Exception {
//        byte[] bytes = page.screenshot();
//        try (FileOutputStream fos = new FileOutputStream("screenshots/" + System.currentTimeMillis() + ".png")) {
//            fos.write(bytes);
//        }
//    }
//
//    private static void screenshot(Element element) throws Exception {
//        byte[] bytes = element.screenshot();
//        try (FileOutputStream fos = new FileOutputStream("screenshots/" + System.currentTimeMillis() + ".png")) {
//            fos.write(bytes);
//        }
//    }
//
//}
