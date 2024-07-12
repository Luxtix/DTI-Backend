package com.luxtix.eventManagementWebsite.auth.controller;


import com.luxtix.eventManagementWebsite.auth.dto.LoginRequestDto;
import com.luxtix.eventManagementWebsite.auth.dto.LoginResponseDto;
import com.luxtix.eventManagementWebsite.auth.entity.UserAuth;
import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.auth.service.AuthService;
import com.luxtix.eventManagementWebsite.response.Response;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping("/api/auth")
@Validated
@Log
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto userLogin) throws IllegalAccessException {
        log.info("User login request received for user: " + userLogin.getEmail());
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                userLogin.getEmail(),
                                userLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAuth userDetails = (UserAuth) authentication.getPrincipal();
        log.info("Token requested for user :" + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities().toArray()[0]);
        LoginResponseDto resp = authService.generateToken(authentication);

        Cookie cookie = new Cookie("Sid", resp.getAccessToken());
        cookie.setMaxAge(6000);
        cookie.setPath("/");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=/; HttpOnly");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        authService.logout();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "Sid=; Path=/; Max-Age=0; HttpOnly");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(Response.successfulResponse("Logout successfully"));
    }
}
