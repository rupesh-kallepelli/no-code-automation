package com.vr.test.runner.slave.scheduler;

import com.vr.test.runner.slave.executor.TestExecutor;
import com.vr.test.runner.slave.registry.TestCaseRegistry;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestScheduleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TestScheduler {
    private final TestCaseRegistry testCaseRegistry;
    private final TestExecutor testExecutor;

    public TestScheduler(
            TestCaseRegistry testCaseRegistry,
            TestExecutor testExecutor
    ) {
        this.testCaseRegistry = testCaseRegistry;
        this.testExecutor = testExecutor;
    }

    public Mono<TestScheduleResponse> scheduleTest(TestCase testCase) {
        String testCaseId = testCaseRegistry.register(testCase);
        return Mono.just(new TestScheduleResponse(testCaseId));
    }

    @Scheduled(fixedDelay = 60000)
    public void executeTestCase() {
        testCaseRegistry.getNewTestcaseIds().forEach(testCaseId -> {

            testCaseRegistry.setTestCaseToRunning(testCaseId);
            TestCase testCase = testCaseRegistry.getTestCase(testCaseId);

            testExecutor.execute(testCase)
                    .doOnSuccess(testStepResults -> log.info("Test step results : {}", testStepResults))
                    .doOnError(throwable -> log.error("Error while execution ", throwable))
                    .subscribe();
        });
    }

}
