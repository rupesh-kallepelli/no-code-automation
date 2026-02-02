package com.vr.test.runner.slave.executor.impl;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.test.runner.slave.exceptions.EventDispatchWithScreenshotException;
import com.vr.test.runner.slave.request.Selector;
import com.vr.test.runner.slave.request.TestCaseStep;
import com.vr.test.runner.slave.response.StepStatus;
import com.vr.test.runner.slave.response.TestStepResult;
import jakarta.validation.Valid;

import java.util.concurrent.Callable;

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
                TestStepResult testStepResult = dispatchWithScreenshot(
                        step.name(),
                        () -> TestExecutorActions.navigate(page, step.value()),
                        page
                );
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
                TestStepResult result = dispatchWithScreenshot(
                        step.name(),
                        () -> TestExecutorActions.click(element),
                        page
                );
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case RIGHT_CLICK -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = dispatchWithScreenshot(
                        step.name(),
                        () -> TestExecutorActions.rightClick(element),
                        page
                );
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case TYPE -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = dispatchWithScreenshot(
                        step.name(),
                        () -> TestExecutorActions.type(element, step.value()),
                        page
                );
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case TYPE_INDIVIDUAL_CHAR -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                TestStepResult result = dispatchWithScreenshot(
                        step.name(),
                        () -> TestExecutorActions.typeIndividualChar(element, step.value()),
                        page
                );
                TestExecutorActions.hideHighlight(element);
                yield result;
            }

            case SCROLL_INTO_VIEW -> {
                Element element = getElement(page, step, false);
                yield dispatchWithScreenshot(step.name(), () -> TestExecutorActions.scrollIntoView(element), page);
            }

            case HIGHLIGHT -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.highlight(element);
                yield dispatchWithScreenshot(
                        step.name(),
                        () -> StepStatus.PASSED,
                        page
                );
            }

            case HIDE_HIGHLIGHT -> {
                Element element = getElement(page, step, false);
                TestExecutorActions.hideHighlight(element);
                yield dispatchWithScreenshot(
                        step.name(),
                        () -> StepStatus.PASSED,
                        page
                );
            }

            case GET_TEXT -> {
                Element element = getElement(page, step, false);
                yield dispatchWithScreenshot(step.name(), () -> TestExecutorActions.getText(element), page);
            }

            case DRAG_AND_DROP -> {
                Element source = getElement(page, step, false);
                Element target = getElement(page, step, true);
                yield dispatchWithScreenshot(step.name(), () -> TestExecutorActions.dragAndDrop(source, target), page);
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

    private static TestStepResult dispatchWithScreenshot(String name, Callable<StepStatus> callable, Page page) {
        try {
            return new TestStepResult(name, callable.call(), page.screenshot());
        } catch (Exception e) {
            throw new EventDispatchWithScreenshotException("Exception while dispatching event and taking screenshot", e);
        }
    }
}
