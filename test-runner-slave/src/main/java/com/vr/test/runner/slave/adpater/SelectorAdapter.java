package com.vr.test.runner.slave.adpater;

import com.vr.cdp.actions.v1.element.selector.Selector;
import com.vr.test.runner.slave.request.TestCaseStep;

public class SelectorAdapter {
    public static Selector adaptToElementSelector(TestCaseStep testCaseStep) {
        return switch (testCaseStep.selector().selectorType()) {
            case CSS -> Selector.selectByCssSelector(testCaseStep.selector().selectorValue());
            case XPATH -> Selector.selectByXPath(testCaseStep.selector().selectorValue());
            case TEXT -> Selector.selectByText(testCaseStep.selector().selectorValue());
            case CLASS -> Selector.selectByClass(testCaseStep.selector().selectorValue());
            case TAG -> Selector.selectByTag(testCaseStep.selector().selectorValue());
        };
    }
}
