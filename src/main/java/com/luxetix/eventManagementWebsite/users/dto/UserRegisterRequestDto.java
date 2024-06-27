package com.luxetix.eventManagementWebsite.users.dto;

import com.luxetix.eventManagementWebsite.users.entity.RolesType;
import com.luxetix.eventManagementWebsite.users.entity.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserRegisterRequestDto {
    @NotBlank(message = "Username is required")
    private String fullname;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String refferal;


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
