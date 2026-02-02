package com.vr.test.runner.slave.service.test;

import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestResult;
import com.vr.test.runner.slave.response.TestScheduleResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface TestService {
    Mono<TestScheduleResponse> register(TestCase testCase);

    void setTestCaseToFailed(String testCaseId);

    void setTestCaseToPassed(String testCaseId);

    void setTestCaseToRunning(String testCaseId);

    Set<String> getPassedTestCaseIds();

    Set<String> getNewTestcaseIds();

    Set<String> getFailedTestCaseIds();

    Set<String> getRunningTestCaseIds();

    TestCase getTestCase(String testCaseId);

    TestResult getTestResult(String testCaseId);

    void updateTestResult(String testCaseId, TestResult testResult);
}
