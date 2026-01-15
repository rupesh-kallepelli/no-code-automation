package com.vr.ai.automation.config;

import com.vr.ai.automation.util.ScreencastSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ScreencastSocketHandler screencastSocketHandler;

    public WebSocketConfig(ScreencastSocketHandler screencastSocketHandler) {
        this.screencastSocketHandler = screencastSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(screencastSocketHandler, "/ws/screencast").setAllowedOrigins("*");
    }
}
