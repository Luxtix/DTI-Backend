package com.luxtix.eventManagementWebsite.users.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {

    @NotBlank(message = "Old password is required")
    @NotNull
    private String oldPassword;


    @NotBlank(message = "New password is required")
    @NotNull
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    @NotNull
    private String confirmPassword;
}
