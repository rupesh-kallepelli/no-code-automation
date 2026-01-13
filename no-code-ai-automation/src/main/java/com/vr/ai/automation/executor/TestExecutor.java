package com.vr.ai.automation.executor;

import com.vr.actions.WebActions;
import com.vr.actions.page.PageActions;
import com.vr.ai.automation.entity.Step;
import com.vr.ai.automation.entity.TestPlan;

import java.io.FileOutputStream;

public class TestExecutor {

    private final WebActions web;
    private final PageActions page;

    public TestExecutor(WebActions web, PageActions page) {
        this.web = web;
        this.page = page;
    }

    public void execute(TestPlan plan) throws Exception {
        page.enable();
        for (Step step : plan.steps) {
            screenshot(page);
            switch (step.action) {

                case NAVIGATE -> {
                    page.navigate(step.value);
                }

                case TYPE -> {
                    web.type(step.selector, step.value);
                }

                case CLICK -> {
                    web.click(step.selector);
                }

//                case WAIT_FOR_VISIBLE -> web.waitForVisible(step.selector, step.timeoutMs);
                case WAIT_TIMEOUT -> Thread.sleep(step.timeoutMs);
                case ASSERT_VISIBLE -> System.out.println("ASSERT " + step.selector);
            }
            screenshot(page);
        }
        page.disable();
    }

    private static void screenshot(PageActions page) throws Exception {
        byte[] bytes = page.screenshotPng();
        try (FileOutputStream fos = new FileOutputStream("screenshot" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }
}
