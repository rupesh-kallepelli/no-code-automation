package com.vr.test.runner.slave.service.test.impl;

import com.vr.test.runner.slave.registry.TestCaseRegistry;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestResult;
import com.vr.test.runner.slave.response.TestScheduleResponse;
import com.vr.test.runner.slave.service.test.TestService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class TestServiceImpl implements TestService {
    private final TestCaseRegistry testCaseRegistry;

    public TestServiceImpl(TestCaseRegistry testCaseRegistry) {
        this.testCaseRegistry = testCaseRegistry;
    }

    @Override
    public Mono<TestScheduleResponse> register(TestCase testCase) {
        //registering a test case
        String testCaseId = testCaseRegistry.register(testCase);
        return Mono.just(new TestScheduleResponse(testCaseId));
    }

    @Override
    public void setTestCaseToFailed(String testCaseId) {
        //updating a test case to failed status
        testCaseRegistry.setTestCaseToFailed(testCaseId);
    }

    @Override
    public void setTestCaseToPassed(String testCaseId) {
        //updating a test case to passed status
        testCaseRegistry.setTestCaseToPassed(testCaseId);
    }

    @Override
    public void setTestCaseToRunning(String testCaseId) {
        // updating test cast to running status
        testCaseRegistry.setTestCaseToRunning(testCaseId);
    }

    @Override
    public Set<String> getPassedTestCaseIds() {
        //retrieving passed test cases
        return testCaseRegistry.getPassedTestcaseIds();
    }

    @Override
    public Set<String> getNewTestcaseIds() {
        //retrieving new test cases
        return testCaseRegistry.getNewTestcaseIds();
    }

    @Override
    public Set<String> getFailedTestCaseIds() {
        //retrieving failed test cases
        return testCaseRegistry.getFailedTestcaseIds();
    }

    @Override
    public Set<String> getRunningTestCaseIds() {
        //retrieving running test cases
        return testCaseRegistry.getRunningTestCaseIds();
    }

    @Override
    public TestCase getTestCase(String testCaseId) {
        //retrieving test case
        return testCaseRegistry.getTestCase(testCaseId);
    }

    @Override
    public TestResult getTestResult(String testCaseId) {
        //retrieving test result
        return testCaseRegistry.getCaseResult(testCaseId);
    }

    @Override
    public void updateTestResult(String testCaseId, TestResult testResult) {
        //updating test result
        testCaseRegistry.updateTestResult(testCaseId, testResult);
    }
}
