package com.vr.authservice.service.impl;

import com.vr.authservice.dto.requests.SignUpRequest;
import com.vr.authservice.dto.responses.SignUpResponse;
import com.vr.authservice.entity.User;
import com.vr.authservice.exception.UserAlreadyPresentException;
import com.vr.authservice.mapper.UserMapper;
import com.vr.authservice.repository.UserRepo;
import com.vr.authservice.service.AuthService;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public SignUpResponse signup(SignUpRequest signUpRequest) {
        if (userRepo.findById(signUpRequest.getUsername()).isPresent())
            throw new UserAlreadyPresentException("User already present, try different username");
        User user = userMapper.toUser(signUpRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        User save = userRepo.save(user);
        return new SignUpResponse(save.getUsername());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findById(username);
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
    }
}
