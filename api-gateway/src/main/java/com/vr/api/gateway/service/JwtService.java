package com.vr.api.gateway.service;

public interface JwtService {
    Boolean isTokenExpired(String token);
}
