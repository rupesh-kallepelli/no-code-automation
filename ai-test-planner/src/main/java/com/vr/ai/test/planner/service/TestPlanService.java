package com.vr.ai.test.planner.service;

import com.vr.ai.test.planner.model.testcase.TestCase;

public interface TestPlanService {
    void consumeAndProcessTest(String message);

    TestCase getTestCase(String message);
}
