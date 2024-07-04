package com.luxtix.eventManagementWebsite.users.dto;


import lombok.Data;

@Data
public class ChangePasswordResponseDto {
    private String email;
    private String password;
}
