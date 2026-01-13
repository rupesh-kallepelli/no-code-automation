package com.vr.cdp.client;

import com.vr.cdp.protocol.command.CDPCommand;

import java.util.function.Consumer;

public interface CDPClient {

    <R> R sendAndWait(CDPCommand<R> command) throws Exception;

    void send(CDPCommand<?> command) throws Exception;

    void onEvent(String method, Consumer<String> handler);

    void close();
}
