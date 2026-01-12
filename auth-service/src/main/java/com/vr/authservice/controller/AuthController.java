package com.vr.authservice.controller;

import com.vr.authservice.dto.requests.LoginRequest;
import com.vr.authservice.dto.requests.RefreshTokenRequest;
import com.vr.authservice.dto.requests.SignUpRequest;
import com.vr.authservice.dto.responses.JwtResponse;
import com.vr.authservice.entity.User;
import com.vr.authservice.service.AuthService;
import com.vr.authservice.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(signUpRequest));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
                );
        String jwtToken = jwtService.generateToken(authentication.getName(), User.Role.USER);
        String refreshTokeToken = jwtService.generateRefreshToken(authentication.getName(), User.Role.USER);
        return ResponseEntity.ok(new JwtResponse(jwtToken, refreshTokeToken));
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenRequest refreshToken) {
        if (jwtService.validateToken(refreshToken.getRefreshToken(), refreshToken.getUsername())) {
            return ResponseEntity.ok(
                    new JwtResponse(jwtService.generateToken(refreshToken.getUsername(), User.Role.USER), refreshToken.getRefreshToken())
            );
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("users")
    public ResponseEntity<?> users() {
        return ResponseEntity.ok(authService.getAllUsers());
    }
}
