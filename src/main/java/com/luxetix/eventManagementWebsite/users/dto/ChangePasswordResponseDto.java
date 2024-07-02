package com.luxetix.eventManagementWebsite.users.dto;


import lombok.Data;

@Data
public class ChangePasswordResponseDto {
    private String email;
    private String password;
}
