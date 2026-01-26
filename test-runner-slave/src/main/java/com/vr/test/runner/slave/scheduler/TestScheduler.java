package com.vr.test.runner.slave.scheduler;

import com.vr.test.runner.slave.registry.TestCaseRegistry;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestScheduleResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TestScheduler {
    private final TestCaseRegistry testCaseRegistry;

    public TestScheduler(TestCaseRegistry testCaseRegistry) {
        this.testCaseRegistry = testCaseRegistry;
    }

    public Mono<TestScheduleResponse> scheduleTest(TestCase testCase) {
        String testCaseId = testCaseRegistry.register(testCase);
        return Mono.just(new TestScheduleResponse(testCaseId));
    }

    @Scheduled(fixedDelay = 60000)
    public void executeTestCase() {

    }

}
