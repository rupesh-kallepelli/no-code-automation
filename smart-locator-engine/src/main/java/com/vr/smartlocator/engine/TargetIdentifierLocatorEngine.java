package com.vr.smartlocator.engine;

import com.vr.smartlocator.engine.model.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class TargetIdentifierLocatorEngine {

    private TargetIdentifierLocatorEngine() {
    }

    private interface Filter extends BiFunction<Elements, String, Elements> {
    }

    private static final Map<Identifier, Filter> FILTERS = new EnumMap<>(Identifier.class);

    static {
        FILTERS.put(Identifier.TEXT, TargetIdentifierLocatorEngine::filterByText);
        FILTERS.put(Identifier.TAG, TargetIdentifierLocatorEngine::filterByTag);
        FILTERS.put(Identifier.PLACEHOLDER, (els, v) -> filterByAttribute(els, "placeholder", v));
        FILTERS.put(Identifier.ARIA_LABEL, (els, v) -> filterByAttribute(els, "aria-label", v));
        FILTERS.put(Identifier.ROLE, (els, v) -> filterByAttribute(els, "role", v));
        FILTERS.put(Identifier.CONTEXT, TargetIdentifierLocatorEngine::filterByContext);
        FILTERS.put(Identifier.INDEX, TargetIdentifierLocatorEngine::pickIndex);
        FILTERS.put(Identifier.ATTRIBUTE, TargetIdentifierLocatorEngine::filterByRawAttribute);
    }

    public static Elements find(Document doc, ElementIdentifiers targets) {

        Elements candidates = doc.getAllElements();

        for (ElementIdentifier t : targets.targets()) {

            Filter filter = FILTERS.get(t.identifier());

            if (filter != null) {
                candidates = filter.apply(candidates, t.value());
            }
        }

        return candidates;
    }

    // ---------- filters ----------

    private static Elements filterByText(Elements elements, String text) {
        Elements result = new Elements();
        for (Element el : elements) {
            if (el.ownText().equalsIgnoreCase(text)) result.add(el);
        }
        return result;
    }

    private static Elements filterByTag(Elements elements, String tag) {
        Elements result = new Elements();
        for (Element el : elements) {
            if (el.tagName().equalsIgnoreCase(tag)) result.add(el);
        }
        return result;
    }

    private static Elements filterByAttribute(Elements elements, String attr, String value) {

        Elements result = new Elements();
        for (Element el : elements) {
            if (value.equalsIgnoreCase(el.attr(attr))) result.add(el);
        }
        return result;
    }

    private static Elements filterByContext(Elements elements, String contextText) {

        Elements result = new Elements();

        for (Element el : elements) {
            Element parent = el.parent();
            while (parent != null) {
                if (parent.text().toLowerCase().contains(contextText.toLowerCase())) {
                    result.add(el);
                    break;
                }
                parent = parent.parent();
            }
        }
        return result;
    }

    private static Elements pickIndex(Elements elements, String indexValue) {

        int index = Integer.parseInt(indexValue);
        Elements result = new Elements();

        if (index > 0 && index <= elements.size()) {
            result.add(elements.get(index - 1));
        }
        return result;
    }

    private static Elements filterByRawAttribute(Elements elements, String expr) {

        String[] parts = expr.split("=");
        if (parts.length != 2) return new Elements();

        return filterByAttribute(elements, parts[0], parts[1]);
    }
}