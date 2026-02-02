package com.vr.test.runner.slave.response;

public record TestStepResult(String name, StepStatus stepStatus, String screenShot) {
}
