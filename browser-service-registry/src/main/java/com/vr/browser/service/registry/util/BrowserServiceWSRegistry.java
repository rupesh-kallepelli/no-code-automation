package com.vr.browser.service.registry.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.vr.browser.service.registry.constants.Constants.BROWSER_SERVICE_WS;

@Component
public class BrowserServiceWSRegistry {
    private final RedisTemplate<String, String> redisTemplate;

    public BrowserServiceWSRegistry(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getSocketUrl(String sessionId) {
        return redisTemplate.opsForValue().get(BROWSER_SERVICE_WS + sessionId);
    }
}
