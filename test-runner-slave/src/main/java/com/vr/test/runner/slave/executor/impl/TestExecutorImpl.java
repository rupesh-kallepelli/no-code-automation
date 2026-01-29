package com.vr.test.runner.slave.executor.impl;

import com.vr.test.runner.slave.executor.TestExecutor;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestStepResult;
import com.vr.test.runner.slave.service.test.PageService;
import com.vr.test.runner.slave.service.test.factory.TestServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class TestExecutorImpl implements TestExecutor {
    private final TestServiceFactory testServiceFactory;

    public TestExecutorImpl(TestServiceFactory testServiceFactory) {
        this.testServiceFactory = testServiceFactory;
    }

    @Override
    public Mono<List<TestStepResult>> execute(TestCase testCase) {
        PageService testService = testServiceFactory.getTestService(testCase.browser());
        return testService.launch().map(page -> {
            try {
                List<TestStepResult> stepResultList = testCase.steps().stream()
                        .map(testCaseStep -> TestStepExecutor.execute(page, testCaseStep))
                        .toList();
                page.close();
                testService.close(page.getId()).subscribe();
                return stepResultList;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).doOnSuccess(
                testStepResults -> testStepResults.forEach(stepResult -> log.info("Executed Steps : {}", stepResult))
        ).doOnError(throwable -> log.error("Error while running tests", throwable));
    }

}
