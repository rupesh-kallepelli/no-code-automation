package com.vr.test.runner.slave.response;

import java.util.List;

public record TestResult(
        List<TestStepResult> testStepResults
) {
}
