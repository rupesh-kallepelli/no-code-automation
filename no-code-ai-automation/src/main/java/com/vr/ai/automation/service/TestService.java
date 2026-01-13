package com.vr.ai.automation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.actions.ActionContext;
import com.vr.actions.WebActions;
import com.vr.actions.page.PageActions;
import com.vr.ai.automation.entity.ActionType;
import com.vr.ai.automation.entity.Step;
import com.vr.ai.automation.entity.TestPlan;
import com.vr.ai.automation.executor.TestExecutor;
import com.vr.ai.automation.planner.AIPlanner;
import com.vr.cdp.client.CDPClient;
import com.vr.cdp.client.ws.RawCDPClient;
import com.vr.launcher.chrome.ChromeInstance;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class TestService {

    private final AIPlanner aiPlanner;
    private final ObjectMapper mapper = new ObjectMapper();

    public TestService(AIPlanner aiPlanner) {
        this.aiPlanner = aiPlanner;
    }

    public String runTest(String testCase) {
        LauncherService launcherService = new LauncherService();
        TestPlan plan = generateTestPlan(testCase);
        System.out.println(plan);
        try {
            return executeTest(launcherService, plan);
        } catch (Exception e) {
            launcherService.closeInstance();
            System.out.println(e.getMessage());
            return runTest(testCase);
        } finally {
            launcherService.closeInstance();
        }
    }

    private String executeTest(LauncherService launcherService, TestPlan plan) throws Exception {
        ChromeInstance instance = launcherService.getChromeInstance();
        CDPClient client = new RawCDPClient(instance.pageWebSocketUrl());
        ActionContext ctx = new ActionContext(client);
        System.out.println(instance.pageWebSocketUrl());

        PageActions page = new PageActions(ctx);
        WebActions web = new WebActions(ctx);

        TestExecutor executor = new TestExecutor(web, page);
        executor.execute(plan);
        return "✅ Test executed successfully";
    }


    private TestPlan generateTestPlan(String testCase) {
        try {
            String jsonPlan = aiPlanner.generatePlan(testCase);
            TestPlan plan = mapper.readValue(jsonPlan, TestPlan.class);
            validatePlan(plan);
            return plan;
        } catch (Exception e) {
            return generateTestPlan(testCase);
        }
    }

    private static final Set<ActionType> ALLOWED_ACTIONS = EnumSet.allOf(ActionType.class);

    private void validatePlan(TestPlan plan) {

        if (plan.steps == null || plan.steps.isEmpty()) {
            throw new RuntimeException("Test plan has no steps");
        }

        for (int i = 0; i < plan.steps.size(); i++) {
            Step step = plan.steps.get(i);

            if (step.action == null) {
                throw new RuntimeException("Step " + i + " has no action");
            }

            if (!ALLOWED_ACTIONS.contains(step.action)) {
                throw new RuntimeException(
                        "Invalid action '" + step.action + "' at step " + i
                );
            }

            // Action-specific rules
            switch (step.action) {
                case NAVIGATE -> {
                    if (step.value == null || step.value.isBlank()) {
                        throw new RuntimeException("NAVIGATE requires value (URL)");
                    }
                }
                case TYPE -> {
                    if (step.selector == null || step.value == null) {
                        throw new RuntimeException("TYPE requires selector and value");
                    }
                }
                case CLICK -> {
                    if (step.selector == null) {
                        throw new RuntimeException("CLICK requires selector");
                    }
                }
                case WAIT_FOR_VISIBLE -> {
                    if (step.selector == null) {
                        throw new RuntimeException("WAIT_FOR_VISIBLE requires selector");
                    }
                }
            }
        }
    }

}
