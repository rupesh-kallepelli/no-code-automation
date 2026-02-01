package com.vr.test.runner.slave.executor.impl;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.test.runner.slave.request.Selector;
import com.vr.test.runner.slave.request.TestCaseStep;
import com.vr.test.runner.slave.response.StepStatus;
import com.vr.test.runner.slave.response.TestStepResult;
import jakarta.validation.Valid;

import static com.vr.test.runner.slave.adpater.SelectorAdapter.adaptToElementSelector;

public class TestStepExecutor {

    public static TestStepResult execute(Page page, @Valid TestCaseStep step) {

        return switch (step.action()) {

            case NAVIGATE -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                TestStepResult testStepResult = TestExecutorActions.navigate(page, step.value());
                page.cast(
                        "jpeg",
                        50,
                        1920,
                        1080
                );
                yield testStepResult;
            }
            case CLICK -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = TestExecutorActions.click(element);
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case RIGHT_CLICK -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = TestExecutorActions.rightClick(element);
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case TYPE -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = TestExecutorActions.type(element, step.value());
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case TYPE_INDIVIDUAL_CHAR -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = TestExecutorActions.typeIndividualChar(element, step.value());
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case SCROLL_INTO_VIEW -> {
                Element element = getElement(page, step, false);
                yield TestExecutorActions.scrollIntoView(element);
            }

            case HIGHLIGHT -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                yield new TestStepResult(StepStatus.PASSED);
            }

            case HIDE_HIGHLIGHT -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.hideHighlight(element);
                yield new TestStepResult(StepStatus.PASSED);
            }

            case GET_TEXT -> {
                Element element = getElement(page, step, false);
                yield TestExecutorActions.getText(element);
            }

            case DRAG_AND_DROP -> {
                Element source = getElement(page, step, false);
                Element target = getElement(page, step, true);
                yield TestExecutorActions.dragAndDrop(source, target);
            }
        };
    }

    private static Element getElement(Page page, TestCaseStep step, boolean isTarget) {
        Element element;
        Selector selector = isTarget ? step.targetSelector() : step.sourceSelector();

        if (step.isWaitRequired())
            element = resolve(page, selector, step.timeoutMs());
        else
            element = resolve(page, selector);
        return element;
    }

    private static Element resolve(Page page, Selector selector) {
        return page.findElement(adaptToElementSelector(selector));
    }

    private static Element resolve(Page page, Selector selector, long millis) {
        return page.findElement(adaptToElementSelector(selector), millis);
    }

}
