package com.vr.ai.automation.entity;

public class Step {

    public ActionType action;
    public String selector;
    public String value;
    public Integer timeoutMs;

    @Override
    public String toString() {
        return "Step{" +
                "action=" + action +
                ", selector='" + selector + '\'' +
                ", value='" + value + '\'' +
                ", timeoutMs=" + timeoutMs +
                '}';
    }
}
