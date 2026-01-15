package com.vr.test.runner.slave.service.executor;

import com.vr.test.runner.slave.request.TestPlan;

public interface TestExecutorService {
    void execute(TestPlan testPlan);
}
