package com.vr.test.runner.slave.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestResult;
import com.vr.test.runner.slave.service.test.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("api/v1")
@Tag(name = "Test Controller", description = "This is controller is used to schedule/register test, fetch test cases on status")
public class TestController {

    private final TestService testService;
    private final ObjectMapper objectMapper;

    public TestController(
            TestService testService,
            ObjectMapper objectMapper
    ) {
        this.testService = testService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("run")
    @Operation(description = "Register the test case to run, once registered it will be picked any of the test runner slave and execute")
    public Mono<?> runTest(@RequestBody @Valid TestCase testCase) {
        try {
            log.debug("Received test case for execution \n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testCase));
            return testService.register(testCase);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing test case to json");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("new")
    @Operation(description = "Fetch new test cases, this will change when a test runner picks up the test case to execute")
    public Flux<Set<String>> getNewTestCaseIds() {
        return Flux.just(testService.getNewTestcaseIds());
    }

    @GetMapping("passed")
    @Operation(description = "Fetch passed test cases")
    public Mono<Set<String>> getPassedTestCaseIds() {
        return Mono.just(testService.getPassedTestCaseIds());
    }

    @GetMapping("failed")
    @Operation(description = "Fetch failed test cases")
    public Flux<Set<String>> getFailedTestCaseIds() {
        return Flux.just(testService.getFailedTestCaseIds());
    }

    @GetMapping("running")
    @Operation(description = "Fetch running test cases")
    public Flux<Set<String>> getRunningTestCaseIds() {
        return Flux.just(testService.getRunningTestCaseIds());
    }

    @GetMapping("testcase/{id}")
    @Operation(description = "Fetch test case on test id")
    public Mono<TestCase> getTestCase(@PathVariable("id") String testCaseId) {
        return Mono.just(testService.getTestCase(testCaseId));
    }

    @GetMapping("result/{id}")
    @Operation(description = "Fetch test result on test id")
    public Mono<TestResult> getTestResult(@PathVariable("id") String testCaseId) {
        return Mono.just(testService.getTestResult(testCaseId));
    }

}
