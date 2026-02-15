package com.vr.ai.test.planner.model.testcase;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TestCase {
    @NotBlank(message = "testName can't be blank or empty or null")
    String testName;
    @NotNull(message = "steps can't be null")
    List<TestCaseStep> steps;
}