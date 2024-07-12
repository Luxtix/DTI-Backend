package com.luxtix.eventManagementWebsite.auth.dto;


import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private Long userId;
    private String email;
    private String role;
}
