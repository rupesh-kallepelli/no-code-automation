package com.vr.test.runner.slave.controller;

import com.vr.actions.page.v1.Page;
import com.vr.test.runner.slave.request.TestPlan;
import com.vr.test.runner.slave.request.enums.Browser;
import com.vr.test.runner.slave.service.test.TestService;
import com.vr.test.runner.slave.service.test.factory.TestServiceFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class TestController {

    @Autowired
    private TestServiceFactory testServiceFactory;

    @PostMapping("run")
    public ResponseEntity<?> runTest(@RequestBody @Valid TestPlan testPlan) {
        return ResponseEntity.ok().body("Test started");
    }

    @GetMapping("test-run")
    public ResponseEntity<?> runTest() throws Exception {
        TestService testService = testServiceFactory.getTestService(Browser.CHROME);
        Page page = testService.launch();
        page.navigate("http://example.com");
        testService.close(page.getId());
        return ResponseEntity.ok().body("Test Completed");
    }

}
