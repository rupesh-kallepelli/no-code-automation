package com.vr.smartlocator.engine.util;


import com.vr.smartlocator.engine.model.Locator;
import com.vr.smartlocator.engine.model.LocatorType;

import java.text.MessageFormat;

public class LocatorFormatter {

    public static Locator cssId(String id) {
        return new Locator(
                LocatorType.CSS,
                MessageFormat.format("#{0}", id)
        );
    }

    public static Locator cssAttr(String tag, String attr, String value) {
        return new Locator(
                LocatorType.CSS,
                MessageFormat.format("{0}[{1}=''{2}'']", tag, attr, value)
        );
    }

    public static Locator cssClass(String tag, String classNames) {
        String normalized = classNames.trim().replace(" ", ".");
        return new Locator(
                LocatorType.CSS,
                MessageFormat.format("{0}.{1}", tag, normalized)
        );
    }

    public static Locator cssContainsText(String tag, String text) {
        return new Locator(
                LocatorType.CSS,
                MessageFormat.format("{0}:containsOwn({1})", tag, text)
        );
    }

    public static Locator cssDescendant(String ancestor, String child) {
        return new Locator(
                LocatorType.CSS,
                MessageFormat.format("{0} {1}", ancestor, child)
        );
    }

    public static Locator xpathIndex(String tag, int index) {
        return new Locator(
                LocatorType.XPATH,
                MessageFormat.format("(//{0})[{1}]", tag, index)
        );
    }

    public static Locator xpathAbsolute(String path) {
        return new Locator(
                LocatorType.ABSOLUTE_XPATH,
                path
        );
    }
}