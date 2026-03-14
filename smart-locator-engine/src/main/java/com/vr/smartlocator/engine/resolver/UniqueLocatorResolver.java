package com.vr.smartlocator.engine.resolver;

import com.vr.smartlocator.engine.model.Locator;
import com.vr.smartlocator.engine.model.LocatorCandidate;
import com.vr.smartlocator.engine.model.LocatorType;
import com.vr.smartlocator.engine.scorer.LocatorScorer;
import com.vr.smartlocator.engine.util.DomPathBuilder;
import com.vr.smartlocator.engine.util.DomUtils;
import com.vr.smartlocator.engine.util.LocatorFormatter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public final class UniqueLocatorResolver {

    private UniqueLocatorResolver() {}

    // ---------- strategy type ----------
    private interface Strategy
            extends BiFunction<Document, Element, Optional<Locator>> {}

    // ---------- static registry ----------
    private static final Map<LocatorType, Strategy> STRATEGIES =
            new EnumMap<>(LocatorType.class);

    static {
        STRATEGIES.put(LocatorType.ID, UniqueLocatorResolver::byId);
        STRATEGIES.put(LocatorType.ATTRIBUTE, UniqueLocatorResolver::byAttribute);
        STRATEGIES.put(LocatorType.TEXT, UniqueLocatorResolver::byText);
        STRATEGIES.put(LocatorType.CONTEXT, UniqueLocatorResolver::byAncestor);
        STRATEGIES.put(LocatorType.INDEX, UniqueLocatorResolver::byIndex);
    }

    // ---------- public API ----------
    public static LocatorCandidate resolve(Document doc, Element target) {

        for (Strategy strategy : STRATEGIES.values()) {

            Optional<Locator> locatorOpt = strategy.apply(doc, target);

            if (locatorOpt.isPresent()) {

                Locator locator = locatorOpt.get();

                if (isUnique(doc, locator)) {
                    return build(doc, target, locator);
                }
            }
        }

        // fallback absolute XPath (always valid)
        Locator absolute = LocatorFormatter.xpathAbsolute(
                DomPathBuilder.build(target)
        );

        return build(doc, target, absolute);
    }

    // ---------- uniqueness (type aware) ----------
    private static boolean isUnique(Document doc, Locator locator) {

        if (locator == null || locator.getValue() == null)
            return false;

        return switch (locator.getType()) {

            // CSS-based locators → validate via JSoup
            case CSS, ID, ATTRIBUTE, TEXT, CONTEXT ->
                    DomUtils.isUniqueCss(doc, locator.getValue());

            // XPath-based locators → skip JSoup validation
            case XPATH, INDEX, ABSOLUTE_XPATH ->
                    true;
        };
    }

    // ---------- strategies ----------

    private static Optional<Locator> byId(Document doc, Element el) {
        return el.hasAttr("id")
                ? Optional.of(LocatorFormatter.cssId(el.id()))
                : Optional.empty();
    }

    private static Optional<Locator> byAttribute(Document doc, Element el) {

        String[] attrs = {"name", "data-testid", "aria-label", "type", "title"};

        for (String a : attrs) {
            if (el.hasAttr(a)) {
                return Optional.of(
                        LocatorFormatter.cssAttr(el.tagName(), a, el.attr(a))
                );
            }
        }
        return Optional.empty();
    }

    private static Optional<Locator> byText(Document doc, Element el) {
        return !el.ownText().isEmpty()
                ? Optional.of(
                        LocatorFormatter.cssContainsText(el.tagName(), el.ownText())
                )
                : Optional.empty();
    }

    private static Optional<Locator> byAncestor(Document doc, Element target) {

        Element ancestor = target.parent();

        while (ancestor != null) {

            Locator ancestorLoc = null;

            if (ancestor.hasAttr("id"))
                ancestorLoc = LocatorFormatter.cssId(ancestor.id());
            else if (!ancestor.className().isEmpty())
                ancestorLoc = LocatorFormatter.cssClass(
                        ancestor.tagName(),
                        ancestor.className()
                );

            if (ancestorLoc != null) {

                Locator child = buildBasicCss(target);

                Locator combined = LocatorFormatter.cssDescendant(
                        ancestorLoc.getValue(),
                        child.getValue()
                );

                if (DomUtils.isUniqueCss(doc, combined.getValue()))
                    return Optional.of(combined);
            }

            ancestor = ancestor.parent();
        }

        return Optional.empty();
    }

    private static Optional<Locator> byIndex(Document doc, Element target) {

        String tag = target.tagName();
        Elements all = doc.getElementsByTag(tag);

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i) == target) {
                return Optional.of(
                        LocatorFormatter.xpathIndex(tag, i + 1)
                );
            }
        }
        return Optional.empty();
    }

    // ---------- helpers ----------

    private static Locator buildBasicCss(Element el) {
        if (!el.className().isEmpty())
            return LocatorFormatter.cssClass(el.tagName(), el.className());

        return new Locator(LocatorType.CSS, el.tagName());
    }

    private static LocatorCandidate build(Document doc,
                                          Element el,
                                          Locator locator) {

        LocatorCandidate c = LocatorCandidate.builder()
                .locator(locator)
                .tag(el.tagName())
                .text(el.ownText())
                .index(DomUtils.indexAmongTag(doc, el))
                .domPath(DomPathBuilder.build(el))
                .build();

        LocatorScorer.score(c);
        return c;
    }
}