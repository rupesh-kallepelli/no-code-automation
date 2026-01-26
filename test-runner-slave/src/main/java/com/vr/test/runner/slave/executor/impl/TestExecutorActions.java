package com.vr.test.runner.slave.executor.impl;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.page.Page;
import com.vr.test.runner.slave.response.StepStatus;
import com.vr.test.runner.slave.response.TestStepResult;

public class TestExecutorActions {
    public static TestStepResult navigate(Page page, String url) {
        return executeEvent(() -> page.navigate(url));
    }

    public static TestStepResult click(Element element) {
        return executeEvent(element::click);
    }

    public static TestStepResult rightClick(Element element) {
        return executeEvent(element::rightClick);
    }

    public static TestStepResult type(Element element, CharSequence charSequence) {
        return executeEvent(() -> element.type(charSequence));
    }

    public static TestStepResult typeIndividualChar(Element element, CharSequence charSequence) {
        return executeEvent(() -> element.typeIndividualCharacter(charSequence));
    }

    public static void highlight(Element element) {
        executeEvent(element::highlight);
    }

    public static void hideHighlight(Element element) {
        executeEvent(element::hideHighlight);
    }

    private static TestStepResult executeEvent(Runnable event) {
        try {
            event.run();
            return new TestStepResult(StepStatus.PASSED);
        } catch (Exception e) {
            return new TestStepResult(StepStatus.FAILED);
        }
    }
}
