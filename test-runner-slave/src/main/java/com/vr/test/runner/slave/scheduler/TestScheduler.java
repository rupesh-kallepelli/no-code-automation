package com.vr.test.runner.slave.scheduler;

import com.vr.test.runner.slave.executor.TestExecutor;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestResult;
import com.vr.test.runner.slave.service.test.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestScheduler {
    private final TestService testService;
    private final TestExecutor testExecutor;

    public TestScheduler(
            TestService testService,
            TestExecutor testExecutor
    ) {
        this.testService = testService;
        this.testExecutor = testExecutor;
    }

    @Scheduled(fixedDelay = 60000)
    public void executeTestCase() {
        testService.getNewTestcaseIds().forEach(testCaseId -> {
            //ignoring if the test is already running or picked by another replica
            if (testService.getRunningTestCaseIds().contains(testCaseId)) {
                log.debug("Test is picked some other slave, moving forward for next test case");
                return;
            }
            //updating the test case status to running
            testService.setTestCaseToRunning(testCaseId);
            TestCase testCase = testService.getTestCase(testCaseId);
            //executing the test case
            testExecutor.execute(testCase)
                    .doOnSuccess(testStepResults -> {
                        //updating the test case result
                        testService.updateTestResult(testCaseId, new TestResult(testStepResults));
                        log.debug("Test step results : {}", testStepResults);
                        //updating the test case status to pass
                        testService.setTestCaseToPassed(testCaseId);
                    })
                    .doOnError(throwable -> {
                        log.error("Error while execution ", throwable);
                        //updating the test case status to fail
                        testService.setTestCaseToFailed(testCaseId);
                    })
                    .subscribe();
        });
    }

}
