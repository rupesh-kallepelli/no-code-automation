package com.vr.test.runner.slave.executor.impl;

import com.vr.actions.v1.page.Page;
import com.vr.test.runner.slave.request.TestCaseStep;
import com.vr.test.runner.slave.response.TestStepResult;
import jakarta.validation.Valid;

public class TestStepExecutor {

    public static TestStepResult execute(Page pag, @Valid TestCaseStep testCaseStep) {
        return switch (testCaseStep.action()) {
            case NAVIGATE -> TestExecutorActions.navigate(pag, testCaseStep.value());
            case CLICK -> {
                TestExecutorActions.highlight(pag.findElement(testCaseStep.selector()));
                TestStepResult testStepResult = TestExecutorActions.click(pag.findElement(testCaseStep.selector()));
                TestExecutorActions.hideHighlight(pag.findElement(testCaseStep.selector()));
                yield testStepResult;
            }
            case TYPE -> {
                TestExecutorActions.highlight(pag.findElement(testCaseStep.selector()));
                TestStepResult testStepResult = TestExecutorActions.type(pag.findElement(testCaseStep.selector()), testCaseStep.value());
                TestExecutorActions.hideHighlight(pag.findElement(testCaseStep.selector()));
                yield testStepResult;
            }
            case WAIT_FOR_VISIBLE -> null;
            case ASSERT_VISIBLE -> null;
            case WAIT_TIMEOUT -> null;
        };
    }
}
