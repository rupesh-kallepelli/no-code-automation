package com.vr.test.runner.slave.controller;

import com.vr.actions.v1.page.Page;
import com.vr.test.runner.slave.request.TestPlan;
import com.vr.test.runner.slave.request.enums.Browser;
import com.vr.test.runner.slave.service.test.TestService;
import com.vr.test.runner.slave.service.test.factory.TestServiceFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class TestController {

    @Autowired
    private TestServiceFactory testServiceFactory;

    @PostMapping("run")
    public Mono<?> runTest(@RequestBody @Valid TestPlan testPlan) {
        return Mono.just("Test started");
    }

    @GetMapping("test-run")
    public Mono<?> runTest() throws Exception {
        TestService testService = testServiceFactory.getTestService(Browser.CHROME);
        Page page = testService.launch();
        page.cast(
                "jpeg",
                50,
                1920,
                1080
        );
        UUID uuid = UUID.randomUUID();
        page.navigate("https://opensource-demo.orangehrmlive.com/");
        page.type("input[name='username']", "Admin");
//        page.screenshot("screenshots/" + uuid + "/" + UUID.randomUUID() + ".png");
        page.type("input[name='password']", "admin123");
//        page.screenshot("screenshots/" + uuid + "/" + UUID.randomUUID() + ".png");
        page.click("button[type='submit']");
        Thread.sleep(5000);
//        page.screenshot("screenshots/" + uuid + "/" + UUID.randomUUID() + ".png");
        testService.close(page.getId());
        return Mono.just("Test Completed");
    }

}
