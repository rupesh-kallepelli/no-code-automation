package com.vr.test.runner.slave.request;

import com.vr.test.runner.slave.request.enums.TestCaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseStatusWrapper {
    TestCase testCase;
    TestCaseStatus testCaseStatus;
}
