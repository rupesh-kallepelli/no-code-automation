package com.vr.browser.service.registry.service.impl;

import com.vr.browser.service.registry.request.RegisterRequest;
import com.vr.browser.service.registry.response.RegistryResponse;
import com.vr.browser.service.registry.service.BrowserRegistryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BrowserRegistryServiceImpl implements BrowserRegistryService {
    private final RedisTemplate<String, String> redisTemplate;

    public BrowserRegistryServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RegistryResponse register(RegisterRequest registerRequest) {
        redisTemplate.opsForList().rightPush("browser-registry", registerRequest.toString());
        String registerRequest1 = redisTemplate.opsForList().rightPop("browser-registry");
        System.out.println(registerRequest1);
        return new RegistryResponse(UUID.randomUUID().toString(), "CONNECTED", "200");
    }
}
