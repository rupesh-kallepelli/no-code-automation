package com.vr.test.runner.slave.executor.impl;

import com.vr.cdp.actions.v1.element.Element;
import com.vr.cdp.actions.v1.page.Page;
import com.vr.test.runner.slave.response.StepStatus;
import com.vr.test.runner.slave.response.StepStatus;


public class TestExecutorActions {

    /* ---------------- PAGE ACTIONS ---------------- */

    public static StepStatus navigate(Page page, String url) {
        return executeEvent(() -> page.navigate(url));
    }

    /* ---------------- ELEMENT ACTIONS ---------------- */

    public static StepStatus click(Element element) {
        return executeEvent(element::click);
    }

    public static StepStatus rightClick(Element element) {
        return executeEvent(element::rightClick);
    }

    public static StepStatus type(Element element, CharSequence charSequence) {
        return executeEvent(() -> element.type(charSequence));
    }

    public static StepStatus typeIndividualChar(Element element, CharSequence charSequence) {
        return executeEvent(() -> element.typeIndividualCharacter(charSequence));
    }

    public static StepStatus scrollIntoView(Element element) {
        return executeEvent(element::scrollIntoView);
    }

    public static StepStatus dragAndDrop(Element source, Element target) {
        return executeEvent(() -> source.dragToElement(target));
    }

    /* ---------------- VISUAL HELPERS ---------------- */

    public static void highlight(Element element) {
        safeExecute(element::highlight);
    }

    public static void hideHighlight(Element element) {
        safeExecute(element::hideHighlight);
    }

    /* ---------------- DATA ACTIONS ---------------- */

    public static StepStatus getText(Element element) {
        return executeEvent(element::getText);
    }

    /* ---------------- CORE EXECUTION ---------------- */

    private static StepStatus executeEvent(Runnable event) {
        try {
            event.run();
            return StepStatus.PASSED;
        } catch (Exception e) {
            return StepStatus.FAILED;
        }
    }

    /**
     * Non-critical helpers must never fail a step.
     */
    private static void safeExecute(Runnable event) {
        try {
            event.run();
        } catch (Exception ignored) {
        }
    }
}

