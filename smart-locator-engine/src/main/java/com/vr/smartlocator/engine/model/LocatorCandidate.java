package com.vr.smartlocator.engine.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocatorCandidate {

    private Locator locator;
    private int score;

    private String domPath;
    private int index;
    private String tag;
    private String text;
}