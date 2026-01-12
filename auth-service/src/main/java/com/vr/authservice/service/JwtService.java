package com.vr.authservice.service;

import com.vr.authservice.entity.User;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String generateToken(String username, User.Role role);

    String generateRefreshToken(String username, User.Role role);

    String extractUsername(String token);

    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Boolean validateToken(String token, String username);
}
