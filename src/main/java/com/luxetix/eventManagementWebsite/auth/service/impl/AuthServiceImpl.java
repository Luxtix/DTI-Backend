package com.luxetix.eventManagementWebsite.auth.service.impl;

import com.luxetix.eventManagementWebsite.auth.helpers.Claims;
import com.luxetix.eventManagementWebsite.auth.repository.AuthRedisRepository;
import com.luxetix.eventManagementWebsite.auth.service.AuthService;
import com.luxetix.eventManagementWebsite.exceptions.InputException;
import com.luxetix.eventManagementWebsite.userUsageRefferals.repository.UserUsageReferralsRepository;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import com.luxetix.eventManagementWebsite.users.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;


@Service
@Log
public class AuthServiceImpl implements AuthService {

    private final JwtEncoder jwtEncoder;

    private final PasswordEncoder passwordEncoder;
    private final AuthRedisRepository authRedisRepository;

    private final UserUsageReferralsRepository userUsageReferralsRepository;
    private final UserRepository userRepository;

    public AuthServiceImpl(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, AuthRedisRepository authRedisRepository, UserUsageReferralsRepository userUsageReferralsRepository, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.authRedisRepository = authRedisRepository;
        this.userUsageReferralsRepository = userUsageReferralsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        long userId = userRepository.findByEmail(authentication.getName()).get().getId();
        boolean exist = userUsageReferralsRepository.findAll().stream().anyMatch(data -> data.getUsers().getId() == userId);
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var existingKey = authRedisRepository.getJwtKey(authentication.getName());
        if(existingKey != null){
            log.info("Token already exists for user: " + authentication.getName());
            return existingKey;
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("isReferral",exist)
                .claim("id",userRepository.findByEmail(authentication.getName()).get().getId())
                .build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        if(authRedisRepository.isKeyBlacklisted(jwt)){
            throw new InputException("JWT Token has already been blacklisted");
        }
        authRedisRepository.saveJwtKey(authentication.getName(),jwt);
        return jwt;
    }

    @Override
    public void logout() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        String jwt = authRedisRepository.getJwtKey(email);
        authRedisRepository.blackListJwt(email,jwt);
        authRedisRepository.deleteJwtKey(email);
    }
}
