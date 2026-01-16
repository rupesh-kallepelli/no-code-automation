package com.vr.test.runner.slave.service.test;

import com.vr.actions.page.v1.Page;
import com.vr.test.runner.slave.request.Selector;

import java.net.URL;

public interface TestService {
    Page launch();

    void close(Long id);

    void navigate(String url);

    void navigate(URL url);

    void click(Selector selector);

    void type(Selector selector, String text);
}
