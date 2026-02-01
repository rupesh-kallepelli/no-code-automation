package com.vr.cdp.actions.v1.browser;

import com.vr.cdp.actions.v1.page.Page;

import java.util.List;

public interface Browser {
    Page getPage();

    String getSessionId();

    Page createPage(String url);

    Page switchToPage(String id);

    boolean deletePage(String id);

    List<Page> getPages();
    void close();
}
