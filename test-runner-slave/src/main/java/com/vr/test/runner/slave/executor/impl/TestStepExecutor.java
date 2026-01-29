package com.vr.test.runner.slave.executor.impl;

import com.vr.cdp.actions.v1.page.Page;
import com.vr.test.runner.slave.request.TestCaseStep;
import com.vr.test.runner.slave.response.TestStepResult;
import jakarta.validation.Valid;

import static com.vr.test.runner.slave.adpater.SelectorAdapter.adaptToElementSelector;

public class TestStepExecutor {

    public static TestStepResult execute(Page page, @Valid TestCaseStep testCaseStep) {
        return switch (testCaseStep.action()) {
            case NAVIGATE -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                TestStepResult testStepResult = TestExecutorActions.navigate(page, testCaseStep.value());
                page.cast(
                        "jpeg",
                        50,
                        1920,
                        1080
                );
                yield testStepResult;
            }
            case CLICK -> {
                TestExecutorActions.highlight(page.findElement(adaptToElementSelector(testCaseStep)));
                TestStepResult testStepResult = TestExecutorActions.click(page.findElement(adaptToElementSelector(testCaseStep)));
                TestExecutorActions.hideHighlight(page.findElement(adaptToElementSelector(testCaseStep)));
                yield testStepResult;
            }
            case TYPE -> {
                TestExecutorActions.highlight(page.findElement(adaptToElementSelector(testCaseStep)));
                TestStepResult testStepResult = TestExecutorActions.type(page.findElement(adaptToElementSelector(testCaseStep)), testCaseStep.value());
                TestExecutorActions.hideHighlight(page.findElement(adaptToElementSelector(testCaseStep)));
                yield testStepResult;
            }
            case WAIT_FOR_VISIBLE -> null;
            case ASSERT_VISIBLE -> null;
            case WAIT_TIMEOUT -> null;
        };
    }
}
