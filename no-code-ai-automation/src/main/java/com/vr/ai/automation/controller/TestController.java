package com.vr.ai.automation.controller;

import com.vr.ai.automation.service.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/run-test")
    public String runTest(@RequestBody String testCase) throws Exception {
        return testService.runTest(testCase);
    }
}

