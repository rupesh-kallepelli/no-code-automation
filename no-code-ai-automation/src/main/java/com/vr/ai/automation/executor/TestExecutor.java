package com.vr.ai.automation.executor;

import com.vr.ai.automation.entity.Step;
import com.vr.ai.automation.entity.TestPlan;
import com.vr.ai.automation.session.BrowserSession;

public class TestExecutor {

    private final BrowserSession browser;

    public TestExecutor(BrowserSession browser) {
        this.browser = browser;
    }

    public void execute(TestPlan plan) throws Exception {

        for (Step step : plan.steps) {
            switch (step.action) {

                case NAVIGATE -> browser.navigate(step.value);

                case TYPE -> browser.type(step.selector, step.value);

                case CLICK -> browser.click(step.selector);

                case WAIT_FOR_VISIBLE -> browser.waitForVisible(step.selector, step.timeoutMs);

                case ASSERT_VISIBLE -> System.out.println("ASSERT " + step.selector);
            }
        }

        browser.close();
    }
}
