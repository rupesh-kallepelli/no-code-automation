package com.vr.ai.test.planner.controller;

import com.vr.ai.test.planner.model.testcase.TestCase;
import com.vr.ai.test.planner.service.TestPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AIPlannerController {
    private final TestPlanService testPlanService;

    @PostMapping("/generate-test")
    public ResponseEntity<TestCase> generateTest(@RequestBody String userInput) {
        return ResponseEntity.ok(testPlanService.getTestCase(userInput));
    }
}
