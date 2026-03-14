package com.vr.smartlocator.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Locator {
    private LocatorType type;
    private String value;
}