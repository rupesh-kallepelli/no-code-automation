package com.vr.ai.test.planner.model.testcase;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TestCase(
        @NotBlank(message = "testName can't be blank or empty or null") String testName,
        @NotNull(message = "TestCaseStep can't be null") List<TestCaseStep> steps) {
}