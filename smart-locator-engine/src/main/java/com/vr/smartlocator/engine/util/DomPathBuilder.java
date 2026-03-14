package com.vr.smartlocator.engine.util;

import org.jsoup.nodes.Element;

public class DomPathBuilder {

    public static String build(Element el) {

        StringBuilder path = new StringBuilder();
        Element current = el;

        while (current != null) {

            int index = 1;
            Element sibling = current.previousElementSibling();

            while (sibling != null) {
                if (sibling.tagName().equals(current.tagName()))
                    index++;
                sibling = sibling.previousElementSibling();
            }

            path.insert(0, "/" + current.tagName() + "[" + index + "]");
            current = current.parent();
        }

        return path.toString();
    }
}