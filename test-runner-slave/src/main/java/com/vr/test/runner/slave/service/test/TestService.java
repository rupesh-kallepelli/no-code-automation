package com.vr.test.runner.slave.service.test;

import com.vr.launcher.BrowserInstance;
import com.vr.test.runner.slave.request.Selector;

import java.net.URL;

public interface TestService {
    BrowserInstance launch();

    void navigate(String url);

    void navigate(URL url);

    void click(Selector selector);

    void type(Selector selector, String text);
}
