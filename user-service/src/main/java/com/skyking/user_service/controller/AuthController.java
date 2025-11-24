package com.skyking.user_service.controller;

import com.skyking.user_service.dto.request.LoginRequest;
import com.skyking.user_service.dto.request.SignupRequest;
import com.skyking.user_service.dto.response.AuthResponse;
import com.skyking.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody SignupRequest req) {
        var user = userService.signup(req);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginRequest req) {
        String token = userService.login(req);
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }
}
