package com.vr.ai.automation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.ai.automation.cdp.BrowserType;
import com.vr.ai.automation.cdp.CdpBrowserSessionV2;
import com.vr.ai.automation.entity.ActionType;
import com.vr.ai.automation.entity.Step;
import com.vr.ai.automation.entity.TestPlan;
import com.vr.ai.automation.executor.TestExecutor;
import com.vr.ai.automation.planner.AIPlanner;
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

        try {
            // 1. Ask AI to generate test plan
            String jsonPlan = aiPlanner.generatePlan(testCase);

            // 2. Parse JSON → TestPlan
            TestPlan plan = mapper.readValue(jsonPlan, TestPlan.class);
            System.out.println(plan);
            validatePlan(plan);
            // 3. Execute
            TestExecutor executor =
                    new TestExecutor(new CdpBrowserSessionV2(
                            BrowserType.EDGE,
                            "",
                            false));

            executor.execute(plan);

            return "✅ Test executed successfully";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "❌ Test failed: " + e.getMessage();
        }
    }

    private static final Set<ActionType> ALLOWED_ACTIONS =
            EnumSet.allOf(ActionType.class);

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
