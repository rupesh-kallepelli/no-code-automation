package com.vr.test.runner.slave.service.test;

import com.vr.actions.v1.page.Page;
import com.vr.test.runner.slave.request.Selector;
import reactor.core.publisher.Mono;

import java.net.URL;

public interface TestService {
    Mono<Page> launch();

    void close(String id);

    void navigate(String url);

    void navigate(URL url);

    void click(Selector selector);

    void type(Selector selector, String text);
}
