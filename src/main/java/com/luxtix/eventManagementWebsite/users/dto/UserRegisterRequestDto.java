package com.luxtix.eventManagementWebsite.users.dto;

import com.luxtix.eventManagementWebsite.users.entity.RolesType;
import com.luxtix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserRegisterRequestDto {
    @NotBlank(message = "fullname is required")
    private String fullname;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Referral is required")
    private String referral;


    @Enumerated(EnumType.STRING)
    private RolesType role;
    public Users toEntity() {
        Users user = new Users();
        user.setRole(role);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullname(fullname);
        return user;
    }
}
