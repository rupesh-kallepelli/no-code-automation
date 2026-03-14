package com.vr.smartlocator.engine.scorer;

import com.vr.smartlocator.engine.model.LocatorCandidate;
import com.vr.smartlocator.engine.model.LocatorType;

import java.util.EnumMap;
import java.util.Map;

public final class LocatorScorer {

    private LocatorScorer() {
    }

    private static final Map<LocatorType, Integer> SCORES = new EnumMap<>(LocatorType.class);

    static {
        SCORES.put(LocatorType.ID, 100);
        SCORES.put(LocatorType.ATTRIBUTE, 90);
        SCORES.put(LocatorType.CONTEXT, 88);
        SCORES.put(LocatorType.TEXT, 85);
        SCORES.put(LocatorType.CSS, 80);
        SCORES.put(LocatorType.XPATH, 70);
        SCORES.put(LocatorType.INDEX, 40);
        SCORES.put(LocatorType.ABSOLUTE_XPATH, 10);
    }

    public static void score(LocatorCandidate c) {

        int score = SCORES.getOrDefault(c.getLocator().getType(), 0);

        c.setScore(score);
    }
}