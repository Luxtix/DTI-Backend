package com.luxetix.eventManagementWebsite.users.dto;


import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
