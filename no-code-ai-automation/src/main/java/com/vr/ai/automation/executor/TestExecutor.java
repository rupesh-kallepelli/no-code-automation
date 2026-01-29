package com.vr.ai.automation.executor;

import com.vr.ai.automation.entity.Step;
import com.vr.ai.automation.entity.TestPlan;
import com.vr.cdp.actions.v1.page.Page;

import java.io.FileOutputStream;

public class TestExecutor {
    private final Page page;

    public TestExecutor(Page page) {
        this.page = page;
    }

    public void execute(TestPlan plan) throws Exception {
//        page.enable();
        for (Step step : plan.steps) {
            screenshot(page);
            switch (step.action) {

                case NAVIGATE -> {
                    page.navigate(step.value);
                }

                case TYPE -> {
//                    page.type(step.selector, step.value);
                }

                case CLICK -> {
//                    page.click(step.selector);
                }

//                case WAIT_FOR_VISIBLE -> web.waitForVisible(step.selector, step.timeoutMs);
                case WAIT_TIMEOUT -> Thread.sleep(step.timeoutMs);
                case ASSERT_VISIBLE -> System.out.println("ASSERT " + step.selector);
            }
            screenshot(page);
        }
    }

    private static void screenshot(Page page) throws Exception {
        byte[] bytes = page.screenshot();
        try (FileOutputStream fos = new FileOutputStream("screenshot" + System.currentTimeMillis() + ".png")) {
            fos.write(bytes);
        }
    }
}
