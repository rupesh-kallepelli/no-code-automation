package com.vr.test.runner.slave.config;

import com.vr.test.runner.slave.util.ScreencastSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ScreencastSocketHandler screencastSocketHandler;

    public WebSocketConfig(ScreencastSocketHandler screencastSocketHandler) {
        this.screencastSocketHandler = screencastSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(screencastSocketHandler, "/ws/screencast")
                .setAllowedOrigins("*");
    }
}
