package com.vr.ai.automation.entity;

import java.util.List;
public class TestPlan {
    @Override
    public String toString() {
        return "TestPlan{" +
                "testName='" + testName + '\'' +
                ", steps=" + steps +
                '}';
    }

    public String testName;
    public List<Step> steps;
}
