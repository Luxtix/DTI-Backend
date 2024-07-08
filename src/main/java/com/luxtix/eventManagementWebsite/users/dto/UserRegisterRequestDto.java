package com.luxtix.eventManagementWebsite.users.dto;

import com.luxtix.eventManagementWebsite.users.entity.RolesType;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserRegisterRequestDto {
    @NotBlank(message = "fullname is required")
    @NotNull
    private String displayName;

    @NotBlank(message = "Email is required")
    @Email
    @NotNull
    private String email;

    @NotBlank(message = "Password is required")
    @NotNull
    private String password;

    @NotNull
    private String referral;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RolesType role;
    public Users toEntity() {
        Users user = new Users();
        user.setRole(role);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullname(displayName);
        return user;
    }
}
