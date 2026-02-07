package com.vr.test.runner.slave.request;

import com.vr.test.runner.slave.request.enums.TestCaseStatus;

import java.util.Set;

public record TestIdResponse(TestCaseStatus testCaseStatus, Set<String> testCaseIds) {
}
