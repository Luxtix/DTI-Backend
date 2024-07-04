package com.luxtix.eventManagementWebsite.auth.service.impl;

import com.luxtix.eventManagementWebsite.auth.helpers.Claims;
import com.luxtix.eventManagementWebsite.auth.repository.AuthRedisRepository;
import com.luxtix.eventManagementWebsite.auth.service.AuthService;
import com.luxtix.eventManagementWebsite.exceptions.InputException;
import com.luxtix.eventManagementWebsite.userUsageRefferals.service.UserUsageReferralsService;
import com.luxtix.eventManagementWebsite.users.service.UserService;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    private final AuthRedisRepository authRedisRepository;

    private final UserUsageReferralsService userUsageReferralsService;
    private final UserService userService;

    public AuthServiceImpl(JwtEncoder jwtEncoder, AuthRedisRepository authRedisRepository, UserUsageReferralsService userUsageReferralsService, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.authRedisRepository = authRedisRepository;
        this.userUsageReferralsService = userUsageReferralsService;
        this.userService = userService;
    }

    @Override
    public String generateToken(Authentication authentication) {
        long userId = userService.getUserByEmail(authentication.getName()).getId();
        Instant expiredDate = Instant.now().minus(90, ChronoUnit.DAYS);
        boolean exist = userUsageReferralsService.checkUserIsReferralAndValid(userId,expiredDate);
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
                .claim("id",userService.getUserByEmail(authentication.getName()).getId())
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
