package com.skyking.user_service.service;


import com.skyking.user_service.domain.User;
import com.skyking.user_service.dto.request.LoginRequest;
import com.skyking.user_service.dto.request.SignupRequest;

public interface UserService {
    User signup(SignupRequest req);
    String login(LoginRequest req); // returns JWT token
}
