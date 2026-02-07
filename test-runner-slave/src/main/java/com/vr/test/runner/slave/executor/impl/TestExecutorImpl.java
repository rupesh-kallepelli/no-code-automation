package com.vr.test.runner.slave.executor.impl;

import com.vr.cdp.actions.v1.page.Page;
import com.vr.test.runner.slave.exceptions.TestExecutionException;
import com.vr.test.runner.slave.executor.TestExecutor;
import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestStepResult;
import com.vr.test.runner.slave.service.test.BrowserService;
import com.vr.test.runner.slave.service.test.factory.TestServiceFactory;
import com.vr.test.runner.slave.util.ScreencastBroadcaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class TestExecutorImpl implements TestExecutor {
    private final TestServiceFactory testServiceFactory;
    private final ScreencastBroadcaster screencastBroadcaster;

    public TestExecutorImpl(TestServiceFactory testServiceFactory, ScreencastBroadcaster screencastBroadcaster) {
        this.testServiceFactory = testServiceFactory;
        this.screencastBroadcaster = screencastBroadcaster;
    }

    @Override
    public Mono<List<TestStepResult>> execute(TestCase testCase) {
        BrowserService testService = testServiceFactory.getTestService(testCase.getBrowser());
        return testService.launch(testCase.getTestCaseId()).map(browser -> {
            try {
                Page page = browser.getPage();
                //executing the test cases
                List<TestStepResult> stepResultList = testCase.getSteps().stream()
                        .map(testCaseStep -> TestStepExecutor.execute(page, testCaseStep))
                        .toList();
                screencastBroadcaster.unregister(testCase.getTestCaseId());
                browser.close();
                testService.close(browser.getSessionId()).subscribe();
                return stepResultList;
            } catch (Exception e) {
                throw new TestExecutionException("Exception while running tests ", e);
            }
        }).doOnSuccess(
                testStepResults -> testStepResults.forEach(stepResult -> log.info("Executed Steps : {}", stepResult))
        ).doOnError(throwable -> log.error("Error while running tests", throwable));
    }

}
