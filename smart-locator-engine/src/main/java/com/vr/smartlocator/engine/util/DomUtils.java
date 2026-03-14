package com.vr.smartlocator.engine.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DomUtils {

    public static boolean isUniqueCss(Document doc, String css) {
        Elements els = doc.select(css);
        return els.size() == 1;
    }

    public static int indexAmongTag(Document doc, Element target) {

        Elements all = doc.getElementsByTag(target.tagName());

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i) == target)
                return i + 1;
        }
        return -1;
    }
}