package com.luxtix.eventManagementWebsite.auth.service;

import com.luxtix.eventManagementWebsite.auth.dto.LoginResponseDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponseDto generateToken(Authentication authentication);

    void logout();
}
