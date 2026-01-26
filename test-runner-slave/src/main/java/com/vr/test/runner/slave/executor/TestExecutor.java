package com.vr.test.runner.slave.executor;

import com.vr.test.runner.slave.request.TestCase;
import com.vr.test.runner.slave.response.TestStepResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TestExecutor {

    Mono<List<TestStepResult>> execute(TestCase testCase);
}
