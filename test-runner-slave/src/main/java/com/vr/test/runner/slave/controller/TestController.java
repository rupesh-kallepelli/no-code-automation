package com.vr.test.runner.slave.controller;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.page.Page;
import com.vr.test.runner.slave.browser.request.BrowserType;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.scheduler.TestScheduler;
import com.vr.test.runner.slave.service.test.TestService;
import com.vr.test.runner.slave.service.test.factory.TestServiceFactory;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;

@RestController
@RequestMapping("api/v1")
public class TestController {

    private final TestServiceFactory testServiceFactory;
    private final TestScheduler testScheduler;

    public TestController(TestServiceFactory testServiceFactory, TestScheduler testScheduler) {
        this.testServiceFactory = testServiceFactory;
        this.testScheduler = testScheduler;
    }

    @PostMapping("run")
    public Mono<?> runTest(@RequestBody @Valid TestCase testCase) {
        return testScheduler.scheduleTest(testCase);
    }

    @GetMapping("test-run")
    public Mono<?> runTest() {
        TestService testService = testServiceFactory.getTestService(BrowserType.CHROME);
        return testService.launch().doOnSuccess(page -> {
            try {
                page.cast(
                        "jpeg",
                        50,
                        1920,
                        1080
                );
                page.navigate("https://opensource-demo.orangehrmlive.com/");
                page.type("input[name='username']", "Admin");
                screenshot(page);
                page.type("input[name='password']", "admin123");
                screenshot(page);
                page.click("button[type='submit']");
                Thread.sleep(5000);
                screenshot(page);
                testService.close(page.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).map(page -> "Test Completed");
    }

    private static void screenshot(Page page) throws Exception {
        byte[] bytes = page.screenshot();
        try (FileOutputStream fos = new FileOutputStream("screenshots/" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }

    private static void screenshot(Element element) throws Exception {
        byte[] bytes = element.screenshot();
        try (FileOutputStream fos = new FileOutputStream("screenshots/" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }

}
