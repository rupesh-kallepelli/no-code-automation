package com.vr.authservice.service;

import com.vr.authservice.dto.requests.SignUpRequest;
import com.vr.authservice.dto.responses.SignUpResponse;
import com.vr.authservice.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AuthService extends UserDetailsService {
    SignUpResponse signup(SignUpRequest signUpRequest);

    List<User> getAllUsers();
}
