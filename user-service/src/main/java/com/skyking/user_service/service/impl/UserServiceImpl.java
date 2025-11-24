package com.skyking.user_service.service.impl;

import com.skyking.user_service.domain.User;
import com.skyking.user_service.dto.request.LoginRequest;
import com.skyking.user_service.dto.request.SignupRequest;
import com.skyking.user_service.repository.UserRepository;
import com.skyking.user_service.service.UserService;
import com.skyking.user_service.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public User signup(SignupRequest req) {
        if (repo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role("ROLE_USER")
                .build();
        return repo.save(u);
    }

    @Override
    public String login(LoginRequest req) {
        User u = repo.findByEmail(req.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtUtil.generateToken(u.getEmail(), u.getRole(), u.getId());
    }
}
