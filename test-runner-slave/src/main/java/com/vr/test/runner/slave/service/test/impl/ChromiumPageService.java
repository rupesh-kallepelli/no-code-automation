package com.vr.test.runner.slave.service.test.impl;

import com.vr.test.runner.slave.request.Selector;
import com.vr.test.runner.slave.service.test.PageService;

import java.net.URL;

public abstract class ChromiumPageService implements PageService {

    @Override
    public void navigate(String url) {
    }

    @Override
    public void navigate(URL url) {

    }

    @Override
    public void click(Selector selector) {

    }

    @Override
    public void type(Selector selector, String text) {

    }
}
