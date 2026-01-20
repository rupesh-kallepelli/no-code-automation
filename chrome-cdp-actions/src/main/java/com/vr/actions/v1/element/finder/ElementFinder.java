package com.vr.actions.v1.element.finder;

import com.vr.actions.v1.element.Element;
import com.vr.actions.v1.element.selector.Selector;

import javax.lang.model.util.Elements;
import java.util.List;

public interface ElementFinder {
    Element findElement(Selector selector);

    List<Elements> findElements(Selector selector);

}
